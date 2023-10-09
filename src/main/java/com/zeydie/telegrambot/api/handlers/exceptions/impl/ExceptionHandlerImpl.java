package com.zeydie.telegrambot.api.handlers.exceptions.impl;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class ExceptionHandlerImpl implements ExceptionHandler {
    @Override
    public void onException(@NotNull final TelegramException telegramException) {
        final BaseResponse baseResponse = telegramException.response();

        if (baseResponse == null) {
            log.error("Network probably!!!");
            return;
        }

        final int code = baseResponse.errorCode();
        final String description = baseResponse.description();

        log.error("{}: {}", code, description);

        if (code == 404) {
            log.error("Bad token");

            System.exit(0);
        }
    }
}