package com.zeydie.telegrambot.core.impl.modules.cache.messages;

import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.telegram.events.handlers.IMessageEventHandler;
import lombok.NonNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;

public class DirectlyMessagesCacheImpl implements IMessagesCache {
    @Autowired
    private IMessageEventHandler messageEventHandler;

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
            this.messageEventHandler.handle(message);
    }
}