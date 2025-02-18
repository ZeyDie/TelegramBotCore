package com.zeydie.telegrambot.core.impl.modules.payment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.internal.LinkedTreeMap;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.payment.IPayment;
import com.zeydie.telegrambot.api.modules.payment.data.PaymentData;
import com.zeydie.telegrambot.core.utils.FileUtil;
import com.zeydie.telegrambot.core.utils.LoggerUtil;
import com.zeydie.telegrambot.exceptions.payment.PaymentRegisteredException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.zeydie.telegrambot.core.utils.ReferencePaths.*;

public class PaymentImpl implements IPayment {
    private final @NotNull Map<String, PaymentData> registeredPayments = Maps.newHashMap();

    @Override
    public void preInit() {
        FileUtil.createFolder(PAYMENT_FOLDER_FILE);
    }

    @SneakyThrows
    @Override
    public void init() {
        @NonNull val httpClient = new OkHttpClient();
        @NonNull val request = new Request.Builder()
                .url(TELEGRAM_CURRENCY_URL)
                .build();
        try (@NonNull val response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LoggerUtil.warn("Failed connect to {}", TELEGRAM_CURRENCY_URL);
                return;
            }

            @Nullable val body = response.body();

            if (body == null)
                return;

            @NonNull val json = body.string();

            LoggerUtil.debug("Body: {}", json);

            SGsonBase.create()
                    .fromJsonToObject(json, Maps.newTreeMap())
                    .forEach(
                            (value, key) -> {
                                @NonNull val code = (String) value;
                                @Nullable val linkedTreeMap = (LinkedTreeMap<String, Object>) key;

                                if (linkedTreeMap == null) return;

                                @NonNull val paymentData = new PaymentData(
                                        String.valueOf(linkedTreeMap.get("code")),
                                        String.valueOf(linkedTreeMap.get("title")),
                                        String.valueOf(linkedTreeMap.get("symbol")),
                                        String.valueOf(linkedTreeMap.get("native")),
                                        String.valueOf(linkedTreeMap.get("thousands_sep")),
                                        String.valueOf(linkedTreeMap.get("decimal_sep")),
                                        Boolean.parseBoolean(String.valueOf(linkedTreeMap.get("symbol_left"))),
                                        Boolean.parseBoolean(String.valueOf(linkedTreeMap.get("space_between"))),
                                        Boolean.parseBoolean(String.valueOf(linkedTreeMap.get("drop_zeros"))),
                                        Double.valueOf(String.valueOf(linkedTreeMap.get("exp"))),
                                        Double.valueOf(String.valueOf(linkedTreeMap.get("min_amount"))),
                                        Double.valueOf(String.valueOf(linkedTreeMap.get("max_amount"))),
                                        Lists.newArrayList()
                                );
                                @NonNull val file = getFileName(paymentData);

                                if (!file.exists()) {
                                    LoggerUtil.info("New Telegram currency {} - {}", code, linkedTreeMap);

                                    SGsonFile.createPretty(file).writeJsonFile(paymentData);
                                }
                            }
                    );
        }
    }

    @Override
    public void postInit() {
        @Nullable val files = PAYMENT_FOLDER_FILE.listFiles();

        if (files != null && files.length > 0)
            Arrays.stream(files)
                    .forEach(
                            file -> {
                                try {
                                    this.register(SGsonFile.create(file).fromJsonToObject(new PaymentData()));
                                } catch (final PaymentRegisteredException exception) {
                                    LoggerUtil.error(exception);
                                }
                            }
                    );
    }

    @Override
    public boolean register(@NonNull final PaymentData paymentData) throws PaymentRegisteredException {
        if (this.isRegistered(paymentData))
            throw new PaymentRegisteredException(paymentData);

        @NonNull val code = paymentData.getCode();
        @NonNull val title = paymentData.getTitle();

        this.registeredPayments.put(code, this.initPaymentFile(paymentData));

        LoggerUtil.info("{} ({}) was registered!", title, code);

        return true;
    }

    @Override
    public @NotNull List<PaymentData> getRegisteredPayments() {
        return this.registeredPayments.values().stream().toList();
    }

    @Override
    public boolean isRegistered(@NonNull final PaymentData paymentData) {
        return this.isRegistered(paymentData.getCode());
    }

    @Override
    public boolean isRegistered(@NonNull final String code) {
        return this.registeredPayments.containsKey(code);
    }

    @Override
    public @Nullable PaymentData getPaymentData(@NonNull final PaymentData paymentData) {
        return this.getPaymentData(paymentData.getCode());
    }

    @Override
    public @Nullable PaymentData getPaymentData(@NonNull final String code) {
        return this.registeredPayments.get(code);
    }

    public @NotNull PaymentData initPaymentFile(@NonNull final PaymentData paymentData) {
        return SGsonFile.createPretty(getFileName(paymentData)).fromJsonToObject(paymentData);
    }

    public static @NotNull File getFileName(@NonNull final PaymentData paymentData) {
        return FileUtil.createFileWithNameAndType(
                PAYMENT_FOLDER_PATH,
                paymentData.getCode() + " - " + paymentData.getTitle(),
                CONFIG_TYPE
        );
    }
}
