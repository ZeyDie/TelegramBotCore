package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.modules.interfaces.ILoading;
import org.jetbrains.annotations.NotNull;

public interface IUpdateEventHandler extends ILoading {
    void handle(@NotNull final Update update);
}