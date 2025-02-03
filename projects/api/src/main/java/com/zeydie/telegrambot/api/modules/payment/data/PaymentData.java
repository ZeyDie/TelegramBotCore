package com.zeydie.telegrambot.api.modules.payment.data;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Currency codes and pay systems supported.
 * https://core.telegram.org/bots/payments/currencies.json
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentData {
    private @NotNull String code;
    private @NotNull String title;
    private @NotNull String symbol;
    private @NotNull String natives;
    private @NotNull String thousands_sep;
    private @NotNull String decimal_sep;
    private boolean symbol_left;
    private boolean space_between;
    private boolean drop_zeros;
    private double exp;
    private double min_amount;
    private double max_amount;

    private @NotNull List<PaymentSystemData> paymentSystems = Lists.newArrayList(
            new PaymentSystemData(
                    "Test Payment System",
                    Strings.EMPTY,
                    Strings.EMPTY
            )
    );
}