package com.zeydie.telegrambot.api.modules.cache.messages.data;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public class ListMessagesData {
    @NotNull
    private List<MessageData> messages = Lists.newArrayList();

    public void add(@NonNull final MessageData messageData) {
        this.messages.add(messageData);
    }
}