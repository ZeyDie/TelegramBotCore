package com.zeydie.telegrambot.api;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import com.zeydie.telegrambot.api.configs.BotChatFileConfig;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Log4j2
public final class TelegramBotApp {
    @Getter
    private static final BotChatFileConfig.Json chatSettings = BotChatFileConfig.getJson();
    @Getter
    private static final Status status = new Status();

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
    private static IUpdateEventHandler updateHandler = new UpdateEventHandlerImpl();
    @Setter
    @Getter
    private static IMessageEventHandler messageHandler = new MessageEventHandlerImpl();
    @Setter
    @Getter
    private static ICallbackQueryEventHandler callbackQueryHandler = new CallbackQueryEventHandlerImpl();

    @Getter
    private static TelegramBot telegramBot;

    @SneakyThrows
    public static void setup(@NotNull final BotFileConfig.Json botFileConfig) {
        final long startTime = System.currentTimeMillis();
        log.info("Starting setup...");

        name = botFileConfig.getName();

        language = chatSettings.isMultiLanguage() ? new MultiLanguageImpl() : new SingleLanguageImpl();
        messagesCache = chatSettings.isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
        userCache = new UserCacheImpl();

        telegramBot = new TelegramBot(botFileConfig.getToken());

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
        messageHandler.load();
        callbackQueryHandler.load();

        status.setUpdatingMessages(true);

        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);

        log.info("Initialized! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Setter
    private static @NotNull UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    private static @NotNull ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(@NotNull final BaseRequest<T, R> baseRequest) {
        return telegramBot.execute(baseRequest);
    }

    @Data
    public static class Status {
        @NonFinal
        private boolean updatingMessages;
    }
}