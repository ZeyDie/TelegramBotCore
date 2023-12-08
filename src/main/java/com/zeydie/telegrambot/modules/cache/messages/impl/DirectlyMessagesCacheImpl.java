package com.zeydie.telegrambot.modules.cache.messages.impl;

import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import lombok.NonNull;

public class DirectlyMessagesCacheImpl implements IMessagesCache {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    }

    @Override
    public void save() {
    }

    @Override
    public void put(@NonNull final MessageData messageData) {
        TelegramBotCore.getInstance().getMessageEventHandler().handle(messageData.message());
    }
}