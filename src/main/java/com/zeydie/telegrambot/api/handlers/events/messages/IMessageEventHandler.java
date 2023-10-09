package com.zeydie.telegrambot.api.handlers.events.messages;

import com.pengrad.telegrambot.model.Message;
import org.jetbrains.annotations.NotNull;

public interface IMessageEventHandler {
    void load();

    void handle(@NotNull final Message message);
}
