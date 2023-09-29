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
public final class AbstractBot {
    @NotNull
    private final String name;
    @NotNull
    private final TelegramBot telegramBot;
    @NotNull

    private final CacheUpdates cacheUpdates;

    @NotNull
    private final Status status;

    public AbstractBot(@NotNull final BotFileConfig.Data botFileConfig) {
        this.name = botFileConfig.getName();
        this.telegramBot = new TelegramBot(botFileConfig.getToken());

        this.cacheUpdates = new CacheUpdates();

        this.status = new Status();

        this.defaultInit();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                cacheUpdates.shutdown();
            }
        });
    }

    private void defaultInit() {
        this.telegramBot.setUpdatesListener(new UpdatesListenerImpl(), new ExceptionHandlerImpl());

        this.cacheUpdates.init();

        this.status.setUpdatingMessages(true);
    }

    @Data
    public static class Status {
        private boolean updatingMessages;
    }
}
