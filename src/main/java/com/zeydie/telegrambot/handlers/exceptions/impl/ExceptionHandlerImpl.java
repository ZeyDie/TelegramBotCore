package com.zeydie.telegrambot.handlers.exceptions.impl;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

public class ExceptionHandlerImpl implements ExceptionHandler {
    @Override
    public void onException(@NonNull final TelegramException telegramException) {
        @Nullable val baseResponse = telegramException.response();

        if (baseResponse == null) {
            LoggerUtil.error("Network probably!!!");
            TelegramBotCore.getInstance().stop();

            return;
        }

        val code = baseResponse.errorCode();
        @NonNull val description = baseResponse.description();

        LoggerUtil.error("{}: {}", code, description);

        if (code == 404) {
            LoggerUtil.error("Bad token");
            TelegramBotCore.getInstance().stop();
        }
    }
}