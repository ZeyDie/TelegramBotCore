package com.zeydie.telegrambot.api;

import com.zeydie.telegrambot.api.configs.BotFileConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class Application {
    @Getter
    private static AbstractBot bot;

    public Application(@NotNull final BotFileConfig.Data botFileConfig) {
        bot = new AbstractBot(botFileConfig);
    }
}
