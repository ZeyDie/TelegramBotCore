package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.modules.interfaces.ILoading;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface IMessageEventHandler extends ILoading {
    void handle(@NonNull final MessageData messageData);

    void handle(@NonNull final Message message);
}