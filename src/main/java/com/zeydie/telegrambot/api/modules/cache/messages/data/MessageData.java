package com.zeydie.telegrambot.api.modules.cache.messages.data;

import com.pengrad.telegrambot.model.Message;
import lombok.NonNull;

public record MessageData(@NonNull Message message) {
}