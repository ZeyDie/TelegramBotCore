package com.zeydie.telegrambot.api.data.cache;

import com.pengrad.telegrambot.model.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public final class ChatMessagesData {
    private List<Message> messages = new ArrayList<>();
}
