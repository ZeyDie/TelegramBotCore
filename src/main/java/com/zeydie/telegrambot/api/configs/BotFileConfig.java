package com.zeydie.telegrambot.api.configs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;


public final class BotFileConfig {
    private static final AbstractFileConfig config = new AbstractFileConfig(Data.class, "bot");
    @Getter
    private static Data data;

    public BotFileConfig() {
        data = config.init();
    }

    @lombok.Data
    @EqualsAndHashCode(callSuper = false)
    public static class Data {
        @NonFinal
        private @NotNull String name = "name";
        @NonFinal
        private @NotNull String token = "token";
        @NonFinal
        private boolean caching = true;
        @NonFinal
        private boolean chatingOlyUsers = true;
    }
}
