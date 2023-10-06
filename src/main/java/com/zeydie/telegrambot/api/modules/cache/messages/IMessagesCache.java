package com.zeydie.telegrambot.api.modules.cache.messages;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.modules.cache.IDataCache;
import org.jetbrains.annotations.NotNull;

public interface IMessagesCache extends IDataCache {
    void put(@NotNull final Message message);
}