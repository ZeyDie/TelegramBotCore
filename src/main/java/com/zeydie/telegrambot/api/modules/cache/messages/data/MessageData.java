package com.zeydie.telegrambot.api.modules.cache.messages.data;

import com.pengrad.telegrambot.model.Message;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public record MessageData(@Nullable Message message) {
}