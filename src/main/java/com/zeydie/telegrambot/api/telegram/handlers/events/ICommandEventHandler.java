package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.modules.interfaces.ILoading;
import org.jetbrains.annotations.NotNull;

public interface ICommandEventHandler extends ILoading {
    void handle(@NotNull final Message message);
}