package com.zeydie.telegrambot.telegram.events;

import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateEvent extends AbstractEvent {
    private final @NotNull Update update;
    private final boolean user;
    private final Object sender;

    public UpdateEvent(@NonNull final Update update) {
        this.update = update;
        this.user = update.channelPost() == null;
        this.sender = this.user ? update.message().from() : update.channelPost().senderChat();
    }
}