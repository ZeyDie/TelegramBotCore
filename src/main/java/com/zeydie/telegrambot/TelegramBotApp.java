package com.zeydie.telegrambot;

import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import com.zeydie.telegrambot.api.handlers.events.language.ILanguageEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.api.telegram.handlers.events.ICallbackQueryEventHandler;
import com.zeydie.telegrambot.api.telegram.handlers.events.ICommandEventHandler;
import com.zeydie.telegrambot.api.telegram.handlers.events.IMessageEventHandler;
import com.zeydie.telegrambot.api.telegram.handlers.events.IUpdateEventHandler;
import com.zeydie.telegrambot.configs.AbstractFileConfig;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.configs.data.BotConfig;
import com.zeydie.telegrambot.configs.data.CachingConfig;
import com.zeydie.telegrambot.exceptions.LanguageNotRegisteredException;
import com.zeydie.telegrambot.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.handlers.events.language.impl.LanguageEventHandlerImpl;
import com.zeydie.telegrambot.handlers.exceptions.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.listeners.impl.UpdatesListenerImpl;
import com.zeydie.telegrambot.modules.cache.messages.impl.CachingMessagesCacheImpl;
import com.zeydie.telegrambot.modules.cache.messages.impl.DirectlyMessagesCacheImpl;
import com.zeydie.telegrambot.modules.cache.users.impl.UserCacheImpl;
import com.zeydie.telegrambot.modules.language.impl.LanguageImpl;
import com.zeydie.telegrambot.modules.permissions.local.UserPermissionsImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.CallbackQueryEventHandlerImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.CommandEventHandlerImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.MessageEventHandlerImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.UpdateEventHandlerImpl;
import com.zeydie.telegrambot.utils.ReflectionUtil;
import com.zeydie.telegrambot.utils.RequestUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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
    private static IPermissions permissions;

    @Setter
    @Getter
    public static @NotNull ILanguageEventHandler languageEventHandler = new LanguageEventHandlerImpl();

    @Setter
    @Getter
    private static @NotNull IUpdateEventHandler updateEventHandler = new UpdateEventHandlerImpl();
    @Setter
    @Getter
    private static @NotNull ICallbackQueryEventHandler callbackQueryEventHandler = new CallbackQueryEventHandlerImpl();
    @Setter
    @Getter
    private static @NotNull IMessageEventHandler messageEventHandler = new MessageEventHandlerImpl();
    @Setter
    @Getter
    private static @NotNull ICommandEventHandler commandEventHandler = new CommandEventHandlerImpl();

    @Getter
    private static TelegramBot telegramBot;

    public static void start() {
        log.debug("Scanning configs...");

        ClassIndex.getAnnotated(ConfigSubscribesRegister.class)
                .forEach(annotatedClass -> {
                            log.debug("{}", annotatedClass);

                            if (annotatedClass.getAnnotation(ConfigSubscribesRegister.class).enable()) {
                                @NotNull final Object annotatedClassInstance = ReflectionUtil.instance(annotatedClass);

                                Arrays.stream(annotatedClassInstance.getClass().getDeclaredFields())
                                        .forEach(field -> {
                                                    if (field.isAnnotationPresent(ConfigSubscribe.class)) {
                                                        @NotNull final ConfigSubscribe configSubscribe = field.getAnnotation(ConfigSubscribe.class);

                                                        @NotNull final Object objectInstance = ReflectionUtil.instance(ReflectionUtil.getClassField(field));
                                                        @NotNull final Object config = !configSubscribe.file() ? objectInstance :
                                                                new AbstractFileConfig(
                                                                        Paths.get(configSubscribe.category().toString(), configSubscribe.path()),
                                                                        objectInstance,
                                                                        configSubscribe.name()
                                                                ).init();

                                                        ReflectionUtil.setValueField(field, annotatedClassInstance, config);
                                                    }
                                                }
                                        );
                            }
                        }
                );
    }

    @SneakyThrows
    public static void setup() {
        setup(
                ConfigStore.getBotConfig(),
                ConfigStore.getCachingConfig()
        );
    }

    @SneakyThrows
    public static void setup(
            @NotNull final BotConfig config,
            @NotNull final CachingConfig cachingConfig
    ) {
        final long startTime = System.currentTimeMillis();
        log.info("Starting setup...");

        name = config.getName();

        language = new LanguageImpl();
        messagesCache = cachingConfig.isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
        userCache = new UserCacheImpl();
        permissions = new UserPermissionsImpl();

        telegramBot = new TelegramBot(config.getToken());

        Runtime.getRuntime().addShutdownHook(new Thread(TelegramBotApp::stop));

        log.info("Setup's successful! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    public static void init() throws LanguageRegisteredException {
        final long startTime = System.currentTimeMillis();
        log.info("Starting initialize...");

        languageEventHandler.load();
        updateEventHandler.load();
        callbackQueryEventHandler.load();
        messageEventHandler.load();
        commandEventHandler.load();

        language.load();
        messagesCache.load();
        userCache.load();
        permissions.load();

        status.setUpdatingMessages(true);

        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);

        log.info("Initialized! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    public static void stop() {
        status.setUpdatingMessages(false);

        messagesCache.save();
        userCache.save();
        permissions.save();

        telegramBot.shutdown();

        shutdown();
    }

    public static void shutdown() {
        System.exit(0);
    }

    @Setter
    @Getter
    private static @NotNull UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    @Getter
    private static @NotNull ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(@NotNull final BaseRequest<T, R> baseRequest) {
        return telegramBot.execute(transforms(baseRequest));
    }

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Cancellable execute(
            @NotNull final T request,
            @NotNull final Callback<T, R> callback
    ) {
        return telegramBot.execute(request, callback);
    }

    @SneakyThrows
    public static @NotNull <T extends BaseRequest<T, R>, R extends BaseResponse> BaseRequest<T, R> transforms(@NotNull final BaseRequest<T, R> baseRequest) {
        @Nullable final String text = (String) RequestUtil.getText(baseRequest);
        @Nullable final Object chatId = RequestUtil.getChatId(baseRequest);

        if (text != null)
            RequestUtil.setValue(
                    baseRequest,
                    RequestUtil.PARAMETER_TEXT,
                    chatId != null ? language.localizeObject(chatId, text) : language.localize(text)
            );

        @Nullable final Keyboard keyboard = (Keyboard) RequestUtil.getKeyboard(baseRequest);

        if (keyboard != null) {
            switch (keyboard) {
                case InlineKeyboardMarkup inlineKeyboardMarkup ->
                        Arrays.stream(inlineKeyboardMarkup.inlineKeyboard()).toList()
                                .forEach(inlineKeyboardButtons -> Arrays.stream(inlineKeyboardButtons).toList()
                                        .forEach(inlineKeyboardButton -> {
                                                    try {
                                                        @NotNull final Field textInlineKeyboardField = inlineKeyboardButton.getClass().getDeclaredField("text");
                                                        @NotNull final String textButton = inlineKeyboardButton.text();

                                                        ReflectionUtil.setValueField(
                                                                textInlineKeyboardField,
                                                                inlineKeyboardButton,
                                                                language.localizeObject(chatId, textButton)
                                                        );
                                                    } catch (final NoSuchFieldException |
                                                                   LanguageNotRegisteredException exception) {
                                                        exception.printStackTrace();
                                                    }
                                                }
                                        )
                                );
                case ReplyKeyboardMarkup replyKeyboardMarkup -> {
                    @NotNull final Field field = replyKeyboardMarkup.getClass().getDeclaredField("keyboard");
                    @Nullable final List<List<KeyboardButton>> replyKeyboardButtonsList = (List<List<KeyboardButton>>) ReflectionUtil.getValueField(field, replyKeyboardMarkup);

                    if (replyKeyboardButtonsList != null)
                        replyKeyboardButtonsList
                                .forEach(replyKeyboardButtons -> replyKeyboardButtons
                                        .forEach(keyboardButton -> {
                                                    try {
                                                        @NotNull final Field textKeyboardField = keyboardButton.getClass().getDeclaredField("text");
                                                        @Nullable final String textButton = (String) ReflectionUtil.getValueField(textKeyboardField, keyboardButton);

                                                        if (textButton != null)
                                                            ReflectionUtil.setValueField(
                                                                    textKeyboardField,
                                                                    keyboardButton,
                                                                    language.localizeObject(chatId, textButton)
                                                            );
                                                    } catch (final NoSuchFieldException |
                                                                   LanguageNotRegisteredException exception) {
                                                        exception.printStackTrace();
                                                    }
                                                }
                                        )
                                );
                }
                default -> throw new IllegalStateException("Unexpected value: " + keyboard);
            }
        }

        return baseRequest;
    }

    @Data
    public static class Status {
        @NonFinal
        private boolean updatingMessages;
    }
}