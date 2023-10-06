package com.zeydie.telegrambot.api.modules.cache.messages.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

@Log
public class DirectlyMessagesCacheImpl implements IMessagesCache {
    @Override
    public void load() {
        // TODO
    }

    @Override
    public void save() {
        // TODO
    }

    @Override
    public void put(@NotNull final Message message) {
        log.info(String.format("[%d] %s", message.chat().id(), message));

        final User user = message.from();

        TelegramBotApp.getUserCache().put(user);
        TelegramBotApp.getUserCache().save();
    }
}