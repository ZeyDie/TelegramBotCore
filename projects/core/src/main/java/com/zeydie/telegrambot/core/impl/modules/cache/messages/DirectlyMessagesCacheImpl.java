package com.zeydie.telegrambot.core.impl.modules.cache.messages;

import com.zeydie.telegrambot.core.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import lombok.NonNull;
import lombok.val;

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
        val message = messageData.message();

        if (message != null)
            TelegramBotCore.getInstance().getMessageEventHandler().handle(message);
    }
}