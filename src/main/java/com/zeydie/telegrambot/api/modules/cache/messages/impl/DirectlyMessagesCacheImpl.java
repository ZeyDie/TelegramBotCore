package com.zeydie.telegrambot.api.modules.cache.messages.impl;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
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
    public void put(@NotNull final Message message) {
        TelegramBotApp.getMessageHandler().handle(message);
    }
}