package com.zeydie.telegrambot.api.modules.cache.messages;

import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.modules.interfaces.IData;
import org.jetbrains.annotations.NotNull;

public interface IMessagesCache extends IData {
    void put(@NotNull final MessageData messageData);
}