package com.zeydie.telegrambot.api.modules.keyboard.impl;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.keyboard.IKeyboard;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserKeyboardImpl implements IKeyboard {
    @NotNull
    private final List<KeyboardButton> rowKeyboardButtonList = new ArrayList<>();

    @Getter
    @NotNull
    private final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(this.rowKeyboardButtonList.toArray(new KeyboardButton[]{}));

    @NotNull
    @Override
    public IKeyboard addButton(@NotNull final KeyboardButton keyboardButton) {
        this.rowKeyboardButtonList.add(keyboardButton);

        return this;
    }

    @NotNull
    @Override
    public IKeyboard completeRow() {
        this.keyboard.addRow(this.rowKeyboardButtonList.toArray(new KeyboardButton[]{}));
        this.rowKeyboardButtonList.clear();

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
        this.completeRow();

        TelegramBotApp.execute(new SendMessage(chatId, text).replyMarkup(this.keyboard));
    }

    @NotNull
    public IKeyboard minimizeButtons(final boolean value) {
        this.keyboard.resizeKeyboard(value);

        return this;
    }
}