package com.zeydie.telegrambot.configs.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@EqualsAndHashCode(callSuper = false)
public final class DonateConfig {
    private boolean enabled = false;
    private int amount = 10;
    @NonFinal
    private @NotNull String currency = "XTR";
    @NonFinal
    private @Nullable String providerToken = "";
    @NonFinal
    private @Nullable String payload = "bot_donate";
}