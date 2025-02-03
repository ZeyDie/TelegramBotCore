package com.zeydie.telegrambot.api.modules.payment;

import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import com.zeydie.telegrambot.api.modules.payment.data.PaymentData;
import com.zeydie.telegrambot.exceptions.payment.PaymentRegisteredException;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IPayment extends IInitialize {
    boolean register(@NonNull final PaymentData paymentData) throws PaymentRegisteredException;

    @NotNull
    List<PaymentData> getRegisteredPayments();

    boolean isRegistered(@NonNull final PaymentData paymentData);

    boolean isRegistered(@NonNull final String code);

    @Nullable
    PaymentData getPaymentData(@NonNull final PaymentData paymentData);

    @Nullable
    PaymentData getPaymentData(@NonNull final String code);
}