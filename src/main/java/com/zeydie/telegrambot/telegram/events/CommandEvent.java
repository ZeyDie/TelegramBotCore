package com.zeydie.telegrambot.telegram.events;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommandEvent extends AbstractEvent {
    private final @NotNull Message message;

    public CommandEvent(@NonNull final Message message) {
        this.message = message;
    }
}
