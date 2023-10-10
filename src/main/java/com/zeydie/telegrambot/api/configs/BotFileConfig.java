package com.zeydie.telegrambot.api.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.CONFIGS_FOLDER;

public final class BotFileConfig {
    @Getter
    private static final @NotNull Json json = new AbstractFileConfig(CONFIGS_FOLDER, new Json(), "bot").init();

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Json {
        @NonFinal
        private @NotNull String name = "name";
        @NonFinal
        private @NotNull String token = "token";
    }
}