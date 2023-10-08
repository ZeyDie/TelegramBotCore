package com.zeydie.telegrambot.api.modules.keyboard.impl;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.zeydie.telegrambot.api.modules.keyboard.IUserKeyboard;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserKeyboardImpl extends AbstractKeyboardImpl implements IUserKeyboard {
    @NotNull
    private final List<KeyboardButton> rowKeyboardButtonList = new ArrayList<>();

    @Getter
    @NotNull
    private final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(this.rowKeyboardButtonList.toArray(new KeyboardButton[]{}));

    @NotNull
    @Override
    public IUserKeyboard addButton(@NotNull final KeyboardButton keyboardButton) {
        this.rowKeyboardButtonList.add(keyboardButton);

        return this;
    }

    @NotNull
    @Override
    public IUserKeyboard completeRow() {
        this.keyboard.addRow(this.rowKeyboardButtonList.toArray(new KeyboardButton[]{}));
        this.rowKeyboardButtonList.clear();

        return this;
    }

    @NotNull
    public IUserKeyboard minimizeButtons(final boolean value) {
        this.keyboard.resizeKeyboard(value);

        return this;
    }

    @Override
    public void sendKeyboard(final long chatId, @NotNull final String text) {
        if (!this.rowKeyboardButtonList.isEmpty())
            this.completeRow();

        super.sendKeyboard(chatId, text);
    }
}