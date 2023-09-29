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

        log.severe(String.format("%d: %s", baseResponse.errorCode(), baseResponse.description()));
    }
}
