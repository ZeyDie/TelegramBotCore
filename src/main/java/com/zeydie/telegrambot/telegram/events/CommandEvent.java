package com.zeydie.telegrambot.telegram.events;

import com.pengrad.telegrambot.model.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommandEvent {
    private final @NotNull Message message;

    public CommandEvent(@NotNull final Message message) {
        this.message = message;
    }
}
