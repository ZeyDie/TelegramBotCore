package com.zeydie.telegrambot.api.modules.cache.messages.data;

import com.pengrad.telegrambot.model.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record ChatMessagesData(@NotNull List<Message> messages) {
    public ChatMessagesData {
        messages = new ArrayList<>();
    }
}