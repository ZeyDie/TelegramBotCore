package com.zeydie.telegrambot.api.modules.cache.messages.data;

import com.pengrad.telegrambot.model.Message;
import org.jetbrains.annotations.NotNull;

public record MessageData(@NotNull Message message) {
}