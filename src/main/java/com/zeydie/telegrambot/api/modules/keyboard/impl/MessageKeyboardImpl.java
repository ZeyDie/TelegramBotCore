package com.zeydie.telegrambot.api.modules.keyboard.impl;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.zeydie.telegrambot.api.modules.keyboard.IMessageKeyboard;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessageKeyboardImpl extends AbstractKeyboardImpl implements IMessageKeyboard {
    @NotNull
    private final List<InlineKeyboardButton> rowKeyboardButtonList = new ArrayList<>();

    @Getter
    @NotNull
    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(this.rowKeyboardButtonList.toArray(new InlineKeyboardButton[]{}));

    @NotNull
    @Override
    public IMessageKeyboard addButton(@NotNull final InlineKeyboardButton keyboardButton) {
        this.rowKeyboardButtonList.add(keyboardButton);

        return this;
    }

    @NotNull
    @Override
    public IMessageKeyboard completeRow() {
        this.keyboard.addRow(this.rowKeyboardButtonList.toArray(new InlineKeyboardButton[]{}));
        this.rowKeyboardButtonList.clear();

        return this;
    }

    @Override
    public void sendKeyboard(final long chatId, @NotNull final String text) {
        if (!this.rowKeyboardButtonList.isEmpty())
            this.completeRow();

        super.sendKeyboard(chatId, text);
    }
}