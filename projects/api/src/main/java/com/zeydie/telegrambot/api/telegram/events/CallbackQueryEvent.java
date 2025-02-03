package com.zeydie.telegrambot.api.telegram.events;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CallbackQueryEvent extends AbstractEvent {
    private final @NotNull CallbackQuery callbackQuery;
    private final User sender;

    public CallbackQueryEvent(@NonNull final CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
        this.sender = callbackQuery.from();
    }
}