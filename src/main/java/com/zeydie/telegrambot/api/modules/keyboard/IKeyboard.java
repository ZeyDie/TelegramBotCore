package com.zeydie.telegrambot.api.modules.keyboard;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.jetbrains.annotations.NotNull;

public interface IKeyboard {
    @NotNull
    Keyboard getKeyboard();

    @NotNull
    IKeyboard addButton(@NotNull final KeyboardButton keyboardButton);

    @NotNull
    IKeyboard completeRow();

    void sendKeyboard(
            @NotNull final User user,
            @NotNull final String text
    );

    void sendKeyboard(
            final long chatId,
            @NotNull final String text
    );
}