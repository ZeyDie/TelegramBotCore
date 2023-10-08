package com.zeydie.telegrambot.api.modules.keyboard.impl;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.zeydie.telegrambot.api.modules.keyboard.IUserKeyboard;
import org.jetbrains.annotations.NotNull;

// TODO
public class MenuKeyboardImpl extends AbstractKeyboardImpl implements IUserKeyboard {
    @NotNull
    @Override
    public Keyboard getKeyboard() {
        return null;
    }

    @NotNull
    @Override
    public IUserKeyboard addButton(@NotNull final KeyboardButton keyboardButton) {
        return this;
    }

    @NotNull
    @Override
    public IUserKeyboard completeRow() {
        return this;
    }
}