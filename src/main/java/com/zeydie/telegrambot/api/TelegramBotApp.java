package com.zeydie.telegrambot.api;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.zeydie.telegrambot.api.cache.CacheMessagesUpdates;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
import com.zeydie.telegrambot.api.handlers.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.api.listeners.impl.UpdatesListenerImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public final class TelegramBotApp {
    private static String name;
    private static TelegramBot telegramBot;

    @Getter
    private static CacheMessagesUpdates cacheMessagesUpdates;
    @Getter
    private static Status status;

    public static void setup(@NotNull final BotFileConfig.Data botFileConfig) {
        name = botFileConfig.getName();
        telegramBot = new TelegramBot(botFileConfig.getToken());

        cacheMessagesUpdates = new CacheMessagesUpdates();
        status = new Status();

        cacheMessagesUpdates.init();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                cacheMessagesUpdates.shutdown();
            }
        });
    }

    public static void init() {
        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);

        status.setUpdatingMessages(true);
    }

    @Setter
    private static UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    private static ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    @Data
    public static class Status {
        private boolean updatingMessages;
    }
}
