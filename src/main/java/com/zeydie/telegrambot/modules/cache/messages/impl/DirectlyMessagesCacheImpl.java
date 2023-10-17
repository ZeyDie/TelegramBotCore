package com.zeydie.telegrambot.modules.cache.messages.impl;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class DirectlyMessagesCacheImpl implements IMessagesCache {
    @Override
    public void load() {
    }

    @Override
    public void save() {
    }

    @Override
    public void put(@NotNull final MessageData messageData) {
        TelegramBotApp.getMessageHandler().handle(messageData.message());
    }
}