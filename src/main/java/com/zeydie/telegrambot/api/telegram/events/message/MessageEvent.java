package com.zeydie.telegrambot.api.telegram.events.message;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageEvent extends AbstractEvent {
    private final @NotNull Message message;

    public MessageEvent(@NotNull final Message message) {
        this.message = message;
    }
}