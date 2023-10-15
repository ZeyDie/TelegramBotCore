package com.zeydie.telegrambot.telegram.events.callback;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CallbackQueryEvent extends AbstractEvent {
    private final @NotNull CallbackQuery callbackQuery;

    public CallbackQueryEvent(@NotNull final CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }
}