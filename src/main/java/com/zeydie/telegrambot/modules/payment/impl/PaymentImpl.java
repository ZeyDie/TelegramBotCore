package com.zeydie.telegrambot.modules.payment.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.internal.LinkedTreeMap;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.payment.IPayment;
import com.zeydie.telegrambot.api.modules.payment.data.PaymentData;
import com.zeydie.telegrambot.api.utils.FileUtil;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import com.zeydie.telegrambot.exceptions.payment.PaymentRegisteredException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.*;

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
        @NonNull val response = httpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            @NonNull val json = response.body().string();

            LoggerUtil.info("Body: {}", json);

            SGsonBase.create()
                    .fromJsonToObject(json, Maps.newTreeMap())
                    .forEach(
                            (value, key) -> {
                                val code = (String) value;
                                val linkedTreeMap = (LinkedTreeMap<String, Object>) key;

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
                                @NonNull val file = FileUtil.createFileWithNameAndType(
                                        PAYMENT_FOLDER_PATH,
                                        code + " - " + paymentData.getTitle(),
                                        CONFIG_TYPE
                                );

                                if (!file.exists()) {
                                    LoggerUtil.info("New Telegram currency {} - {}", code, linkedTreeMap);

                                    SGsonFile.createPretty(file).writeJsonFile(paymentData);
                                }
                            }
                    );
        } else LoggerUtil.warn("Failed connect to {}", TELEGRAM_CURRENCY_URL);
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
                                    exception.printStackTrace(System.out);
                                }
                            }
                    );
    }

    @Override
    public boolean register(@NonNull PaymentData paymentData) throws PaymentRegisteredException {
        return false;
    }

    @Override
    public @NotNull List<PaymentData> getRegisteredPayments() {
        return null;
    }

    @Override
    public boolean isRegistered(@NonNull PaymentData paymentData) {
        return false;
    }

    @Override
    public boolean isRegistered(@NonNull String code) {
        return false;
    }

    @Override
    public @Nullable PaymentData getPaymentData(@NonNull PaymentData paymentData) {
        return null;
    }

    @Override
    public @Nullable PaymentData getPaymentData(@NonNull String code) {
        return null;
    }
}
