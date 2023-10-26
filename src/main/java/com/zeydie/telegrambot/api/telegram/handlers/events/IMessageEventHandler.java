package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.modules.interfaces.ILoading;
import org.jetbrains.annotations.NotNull;

public interface IMessageEventHandler extends ILoading {
    void handle(@NotNull final MessageData messageData);

    void handle(@NotNull final Message message);
}