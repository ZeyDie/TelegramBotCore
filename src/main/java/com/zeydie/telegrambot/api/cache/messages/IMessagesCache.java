package com.zeydie.telegrambot.api.cache.messages;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.cache.IDataCache;
import org.jetbrains.annotations.NotNull;

public interface IMessagesCache extends IDataCache {
    void put(@NotNull final Message message);
}
