package com.zeydie.telegrambot.api;

import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import com.zeydie.telegrambot.api.configs.AbstractFileConfig;
import com.zeydie.telegrambot.api.configs.data.BotChatFileConfig;
import com.zeydie.telegrambot.api.configs.data.BotFileConfig;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import com.zeydie.telegrambot.api.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.api.handlers.events.callbacks.ICallbackQueryEventHandler;
import com.zeydie.telegrambot.api.handlers.events.callbacks.impl.CallbackQueryEventHandlerImpl;
import com.zeydie.telegrambot.api.handlers.events.messages.IMessageEventHandler;
import com.zeydie.telegrambot.api.handlers.events.messages.impl.MessageEventHandlerImpl;
import com.zeydie.telegrambot.api.handlers.events.updates.IUpdateEventHandler;
import com.zeydie.telegrambot.api.handlers.events.updates.impl.UpdateEventHandlerImpl;
import com.zeydie.telegrambot.api.handlers.exceptions.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.api.listeners.impl.UpdatesListenerImpl;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.impl.CachingMessagesCacheImpl;
import com.zeydie.telegrambot.api.modules.cache.messages.impl.DirectlyMessagesCacheImpl;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.impl.UserCacheImpl;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.language.impl.MultiLanguageImpl;
import com.zeydie.telegrambot.api.modules.language.impl.SingleLanguageImpl;
import com.zeydie.telegrambot.api.utils.ReflectionUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.Arrays;

@Log4j2
public final class TelegramBotApp {
    @Getter
    private static final @NotNull Status status = new Status();

    @Getter
    private static String name;

    @Setter
    @Getter
    private static ILanguage language;
    @Setter
    @Getter
    private static IMessagesCache messagesCache;
    @Setter
    @Getter
    private static IUserCache userCache;

    @Setter
    @Getter
    private static @NotNull IUpdateEventHandler updateHandler = new UpdateEventHandlerImpl();
    @Setter
    @Getter
    private static @NotNull ICallbackQueryEventHandler callbackQueryHandler = new CallbackQueryEventHandlerImpl();
    @Setter
    @Getter
    private static @NotNull IMessageEventHandler messageHandler = new MessageEventHandlerImpl();

    @Getter
    private static TelegramBot telegramBot;

    public static void start() {
        log.debug("Scanning configs...");

        ClassIndex.getAnnotated(ConfigSubscribesRegister.class)
                .forEach(annotatedClass -> {
                            log.debug("{}", annotatedClass);

                            if (annotatedClass.getAnnotation(ConfigSubscribesRegister.class).enable()) {
                                @NotNull final Object annotatedClassInstance = ReflectionUtil.instance(annotatedClass);

                                Arrays.stream(annotatedClass.getDeclaredFields())
                                        .forEach(field -> {
                                                    if (field.isAnnotationPresent(ConfigSubscribe.class)) {
                                                        log.debug("{}", field);

                                                        @NotNull final ConfigSubscribe configSubscribe = field.getAnnotation(ConfigSubscribe.class);

                                                        @NotNull final Object objectInstance = ReflectionUtil.instance(ReflectionUtil.getClassField(field));
                                                        @NotNull final Object config = !configSubscribe.file() ? objectInstance :
                                                                new AbstractFileConfig(
                                                                        Paths.get(configSubscribe.category().toString(), configSubscribe.path()),
                                                                        objectInstance,
                                                                        configSubscribe.name()
                                                                ).init();

                                                        ReflectionUtil.setValueField(field, annotatedClassInstance, config);

                                                        log.debug("{}", config);
                                                    }
                                                }
                                        );
                            }
                        }
                );
    }

    @SneakyThrows
    public static void setup(
            @NotNull final BotFileConfig config,
            @NotNull final BotChatFileConfig chatSettings
    ) {
        final long startTime = System.currentTimeMillis();
        log.info("Starting setup...");

        name = config.getName();

        language = chatSettings.isMultiLanguage() ? new MultiLanguageImpl() : new SingleLanguageImpl();
        messagesCache = chatSettings.isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
        userCache = new UserCacheImpl();

        telegramBot = new TelegramBot(config.getToken());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            messagesCache.save();
            userCache.save();
        }));

        log.info("Setup's successful! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    public static void init() throws LanguageRegisteredException {
        final long startTime = System.currentTimeMillis();
        log.info("Starting initialize...");

        language.load();
        messagesCache.load();
        userCache.load();

        updateHandler.load();
        callbackQueryHandler.load();
        messageHandler.load();

        status.setUpdatingMessages(true);

        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);

        log.info("Initialized! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Setter
    @Getter
    private static @NotNull UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    @Getter
    private static @NotNull ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(@NotNull final BaseRequest<T, R> baseRequest) {
        return telegramBot.execute(baseRequest);
    }

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Cancellable execute(
            @NotNull final T request,
            @NotNull final Callback<T, R> callback
    ) {
        return telegramBot.execute(request, callback);
    }

    @Data
    public static class Status {
        @NonFinal
        private boolean updatingMessages;
    }
}