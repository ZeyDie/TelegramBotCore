package com.zeydie.telegrambot.api;

import com.pengrad.telegrambot.TelegramBot;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
import com.zeydie.telegrambot.api.cache.chat.CacheUpdates;
import com.zeydie.telegrambot.api.handlers.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.api.listeners.impl.UpdatesListenerImpl;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
@Getter
public class AbstractBot {
    private final String name;
    private final TelegramBot telegramBot;

    private final CacheUpdates cacheUpdates;

    private final Status status;

    public AbstractBot(@NotNull final BotFileConfig botFileConfig) {
        this.name = botFileConfig.getName();
        this.telegramBot = new TelegramBot(botFileConfig.getToken());

        this.cacheUpdates = new CacheUpdates();

        this.status = new Status();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                cacheUpdates.shutdown();
            }
        });
    }

    protected void defaultInit() {
        this.telegramBot.setUpdatesListener(new UpdatesListenerImpl(), new ExceptionHandlerImpl());

        this.status.setUpdatingMessages(true);
    }

    @Data
    public static class Status {
        private boolean updatingMessages;
    }
}
