package com.zeydie.telegrambot.api.modules.cache.messages.data;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record ListMessagesData(@Nullable List<MessageData> messages) {
    public ListMessagesData {
        if (messages == null)
            messages = new ArrayList<>();
    }

    public void add(@NonNull final MessageData messageData) {
        this.messages.add(messageData);
    }
}