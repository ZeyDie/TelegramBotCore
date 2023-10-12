package com.zeydie.telegrambot.api.configs.data;

import lombok.Data;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@Data
public final class BotFileConfig {
    @NonFinal
    private @NotNull String name = "name";
    @NonFinal
    private @NotNull String token = "token";
}