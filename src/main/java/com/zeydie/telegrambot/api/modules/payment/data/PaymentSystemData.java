package com.zeydie.telegrambot.api.modules.payment.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@Data
@RequiredArgsConstructor
public class PaymentSystemData {
    @NonFinal
    private boolean enable;
    @NonFinal
    private final @NotNull String name;
    @NonFinal
    private final @NotNull String providerToken;
    @NonFinal
    private final @NotNull String payload;
}