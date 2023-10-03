package com.zeydie.telegrambot.api.modules.cache.messages.data;

import com.pengrad.telegrambot.model.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatMessagesData {
    private List<Message> messages = new ArrayList<>();
}