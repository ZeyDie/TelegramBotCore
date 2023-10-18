package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.Message;
import org.jetbrains.annotations.NotNull;

public interface ICommandEventHandler {
    void load();

    void handle(@NotNull final Message message);
}
