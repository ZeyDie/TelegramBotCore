package com.zeydie.telegrambot.api.modules.keyboard.impl;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.zeydie.telegrambot.api.modules.keyboard.IUserKeyboard;
import org.jetbrains.annotations.NotNull;

// TODO
public class MenuKeyboardImpl extends AbstractKeyboardImpl implements IUserKeyboard {
    @Override
    public @NotNull Keyboard getKeyboard() {
        return null;
    }

    @Override
    public @NotNull IUserKeyboard addButton(@NotNull final KeyboardButton keyboardButton) {
        return this;
    }

    @Override
    public @NotNull IUserKeyboard completeRow() {
        return this;
    }
}