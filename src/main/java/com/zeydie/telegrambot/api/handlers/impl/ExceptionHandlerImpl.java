package com.zeydie.telegrambot.api.handlers.impl;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

@Log
public class ExceptionHandlerImpl implements ExceptionHandler {
    @Override
    public void onException(@NotNull final TelegramException telegramException) {
        final BaseResponse baseResponse = telegramException.response();

        if (baseResponse == null) {
            log.severe("Network probably!!!");
            return;
        }

        final int code = baseResponse.errorCode();
        final String description = baseResponse.description();

        log.severe(String.format("%d: %s", code, description));

        if (code == 404) {
            log.severe("Bad token");

            System.exit(0);
        }
    }
}
