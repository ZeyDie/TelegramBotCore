package com.zeydie.telegrambot.api.data.cache;

import com.pengrad.telegrambot.model.Message;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
public final class ChatMessagesData {
    @NotNull
    private List<Message> messages = new ArrayList<>();
}
