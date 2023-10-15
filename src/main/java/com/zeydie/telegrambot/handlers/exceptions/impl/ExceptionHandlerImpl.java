package com.zeydie.telegrambot.handlers.exceptions.impl;

import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Log4j2
public class ExceptionHandlerImpl implements com.pengrad.telegrambot.ExceptionHandler {
    @Override
    public void onException(@NotNull final TelegramException telegramException) {
        @Nullable final BaseResponse baseResponse = telegramException.response();

        if (baseResponse == null) {
            log.error("Network probably!!!");
            return;
        }

        final int code = baseResponse.errorCode();
        @NotNull final String description = baseResponse.description();

        log.error("{}: {}", code, description);

        if (code == 404) {
            log.error("Bad token");

            System.exit(0);
        }
    }
}