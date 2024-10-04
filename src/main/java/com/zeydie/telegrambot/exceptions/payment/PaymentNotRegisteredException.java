package com.zeydie.telegrambot.exceptions.payment;

import com.zeydie.telegrambot.api.modules.payment.data.PaymentData;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Getter
public final class PaymentNotRegisteredException extends Exception {
    private final @NotNull String message;

    public PaymentNotRegisteredException(@NonNull final PaymentData paymentData) {
        this(paymentData.getCode(), paymentData.getTitle());
    }

    public PaymentNotRegisteredException(
            @NonNull final String code,
            @NonNull final String title
    ) {
        this.message = String.format("%s (%s) is not registered!", title, code);
    }
}