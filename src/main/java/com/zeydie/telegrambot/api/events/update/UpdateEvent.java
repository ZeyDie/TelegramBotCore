package com.zeydie.telegrambot.api.events.update;

import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateEvent extends AbstractEvent {
    private final @NotNull Update update;

    public UpdateEvent(@NotNull final Update update) {
        this.update = update;
    }
}
