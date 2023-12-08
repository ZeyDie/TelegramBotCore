package com.zeydie.telegrambot.api.modules.cache.messages;

import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.modules.interfaces.IData;
import lombok.NonNull;

public interface IMessagesCache extends IData {
    void put(@NonNull final MessageData messageData);
}