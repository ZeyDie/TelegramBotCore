package com.zeydie.telegrambot.api.telegram.events.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import lombok.NonNull;

public interface ICallbackQueryEventHandler extends IInitialize {
    void handle(@NonNull final CallbackQuery callbackQuery);
}