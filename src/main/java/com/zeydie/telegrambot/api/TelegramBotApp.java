package com.zeydie.telegrambot.api;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.zeydie.telegrambot.api.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.cache.messages.impl.CachingMessagesCacheImpl;
import com.zeydie.telegrambot.api.cache.messages.impl.DirectlyMessagesCacheImpl;
import com.zeydie.telegrambot.api.cache.users.IUserCache;
import com.zeydie.telegrambot.api.cache.users.impl.UserCacheImpl;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
import com.zeydie.telegrambot.api.handlers.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.api.listeners.impl.UpdatesListenerImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

public final class TelegramBotApp {
    @Getter
    private static String name;
    @Getter
    private static boolean chatingOlyUsers;

    @Setter
    @Getter
    private static IMessagesCache messagesCache;
    @Setter
    @Getter
    private static IUserCache userCache;

    @Getter
    private static Status status = new Status();

    @Getter
    private static TelegramBot telegramBot;

    public static void setup(@NotNull final BotFileConfig.Data botFileConfig) {
        name = botFileConfig.getName();
        chatingOlyUsers = botFileConfig.isChatingOlyUsers();

        messagesCache = botFileConfig.isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
        userCache = new UserCacheImpl();

        telegramBot = new TelegramBot(botFileConfig.getToken());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            messagesCache.shutdown();
            userCache.shutdown();
        }));
    }

    public static void init() {
        messagesCache.init();
        userCache.init();

        status.setUpdatingMessages(true);

        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);
    }

    @Setter
    private static UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    private static ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    @Data
    public static class Status {
        @NonFinal
        private boolean updatingMessages;
    }
}
