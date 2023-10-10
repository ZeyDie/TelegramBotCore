package com.zeydie.telegrambot.api.modules.cache.messages.data;

import com.pengrad.telegrambot.model.Message;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record ChatMessagesData(@Nullable List<Message> messages) {
    public ChatMessagesData {
        if (messages == null)
            messages = new ArrayList<>();
    }
}