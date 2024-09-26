package com.zeydie.telegrambot.configs.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public final class BotConfig {
    @NonFinal
    private @NotNull String name = "name";
    @NonFinal
    private @NotNull String token = "token";
    @NonFinal
    private boolean enableReloadCommand = true;
    @NonFinal
    private boolean debug = true;
    @NonFinal
    private @NonNull TestConfig testConfig = new TestConfig();

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class TestConfig {
        private long chatId = 0;
    }
}