package com.zeydie.telegrambot.api.modules.keyboard.impl;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.zeydie.telegrambot.api.modules.keyboard.IKeyboard;
import org.jetbrains.annotations.NotNull;

// TODO
public class MessageKeyboardImpl implements IKeyboard {
    @NotNull
    @Override
    public Keyboard getKeyboard() {
        return null;
    }

    @NotNull
    @Override
    public IKeyboard addButton(@NotNull final KeyboardButton keyboardButton) {
        return this;
    }

    @NotNull
    @Override
    public IKeyboard completeRow() {
        return this;
    }

    @Override
    public void sendKeyboard(
            @NotNull final User user,
            @NotNull final String text
    ) {
        this.sendKeyboard(user.id(), text);
    }

    @Override
    public void sendKeyboard(
            final long chatId,
            @NotNull final String text
    ) {

    }
}