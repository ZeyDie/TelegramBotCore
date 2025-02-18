package com.zeydie.telegrambot.core.impl.handlers.exceptions;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import com.zeydie.telegrambot.core.configs.ConfigStore;
import com.zeydie.telegrambot.core.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ExceptionHandlerImpl implements ExceptionHandler {
    @Override
    public void onException(@NonNull final TelegramException telegramException) {
        @Nullable val baseResponse = telegramException.response();

        if (baseResponse == null) {
            LoggerUtil.error("Network probably!!!");
            return;
        }

        val code = baseResponse.errorCode();
        @NonNull val description = baseResponse.description();

        LoggerUtil.error("{}: {}", code, description);

        if (code == 404) {
            LoggerUtil.error("Bad token {}", ConfigStore.getBotConfig().getToken());

            Runtime.getRuntime().exit(0);
        }
    }
}