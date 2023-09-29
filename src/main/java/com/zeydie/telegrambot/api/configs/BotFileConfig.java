package com.zeydie.telegrambot.api.configs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.NonFinal;


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
        private String name = "name";
        @NonFinal
        private String token = "token";
    }
}
