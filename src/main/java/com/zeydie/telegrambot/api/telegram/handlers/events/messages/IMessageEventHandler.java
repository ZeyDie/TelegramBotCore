package com.zeydie.telegrambot.api.telegram.handlers.events.messages;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import org.jetbrains.annotations.NotNull;

public interface IMessageEventHandler {
    void load();

    void handle(@NotNull final MessageData messageData);

    void handle(@NotNull final Message message);
}