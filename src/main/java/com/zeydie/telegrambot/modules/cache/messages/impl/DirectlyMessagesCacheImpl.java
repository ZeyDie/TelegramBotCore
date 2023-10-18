package com.zeydie.telegrambot.modules.cache.messages.impl;

import com.zeydie.telegrambot.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import org.jetbrains.annotations.NotNull;

public class DirectlyMessagesCacheImpl implements IMessagesCache {
    @Override
    public void load() {
    }

    @Override
    public void save() {
    }

    @Override
    public void put(@NotNull final MessageData messageData) {
        TelegramBotApp.getMessageEventHandler().handle(messageData.message());
    }
}