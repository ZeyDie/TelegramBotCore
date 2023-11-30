package com.zeydie.telegrambot.handlers.exceptions.impl;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import com.zeydie.telegrambot.TelegramBotCore;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.Nullable;

@Log4j2
public class ExceptionHandlerImpl implements ExceptionHandler {
    @Override
    public void onException(@NonNull final TelegramException telegramException) {
        @Nullable val baseResponse = telegramException.response();

        if (baseResponse == null) {
            log.error("Network probably!!!");
            TelegramBotCore.getInstance().stop();
            return;
        }

        val code = baseResponse.errorCode();
        @NonNull val description = baseResponse.description();

        log.error("{}: {}", code, description);

        if (code == 404) {
            log.error("Bad token");

            TelegramBotCore.getInstance().stop();
        }
    }
}