package com.zeydie.telegrambot.api;

import com.zeydie.telegrambot.api.configs.BotFileConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public final class Application {
    private final Timer timer = new Timer();

    @Getter
    private static AbstractBot bot;

    public Application(@NotNull final BotFileConfig botFileConfig) {
        this.bot = new AbstractBot(botFileConfig);

        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

            }
        }, 0, 1000);
    }
}
