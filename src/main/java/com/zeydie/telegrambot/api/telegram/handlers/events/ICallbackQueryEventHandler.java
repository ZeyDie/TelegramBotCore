package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.CallbackQuery;
import org.jetbrains.annotations.NotNull;

public interface ICallbackQueryEventHandler {
    void load();

    void handle(@NotNull final CallbackQuery callbackQuery);
}