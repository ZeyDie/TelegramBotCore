package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.modules.interfaces.ILoading;
import org.jetbrains.annotations.NotNull;

public interface ICallbackQueryEventHandler extends ILoading {
    void handle(@NotNull final CallbackQuery callbackQuery);
}