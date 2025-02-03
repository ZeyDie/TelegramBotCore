package com.zeydie.telegrambot.core.impl.modules.keyboard;

import com.google.common.collect.Lists;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.zeydie.telegrambot.api.modules.keyboard.IUserKeyboard;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserKeyboardImpl extends AbstractKeyboardImpl implements IUserKeyboard {
    public UserKeyboardImpl(@NotNull final String text) {
        super(text);
    }

    public static @NotNull UserKeyboardImpl create(@NonNull final String text) {
        return new UserKeyboardImpl(text);
    }

    private final @NotNull List<KeyboardButton> rowKeyboardButtonList = Lists.newArrayList();

    @Getter
    private final @NotNull ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(this.rowKeyboardButtonList.toArray(new KeyboardButton[]{}));

    @Override
    public @NotNull IUserKeyboard addButton(@NonNull final KeyboardButton keyboardButton) {
        this.rowKeyboardButtonList.add(keyboardButton);

        return this;
    }

    @Override
    public @NotNull IUserKeyboard completeRow() {
        this.keyboard.addRow(this.rowKeyboardButtonList.toArray(new KeyboardButton[]{}));
        this.rowKeyboardButtonList.clear();

        return this;
    }

    public @NotNull IUserKeyboard minimizeButtons(final boolean value) {
        this.keyboard.resizeKeyboard(value);

        return this;
    }

    @Override
    public void sendKeyboard(final long chatId, @NonNull final String text) {
        if (!this.rowKeyboardButtonList.isEmpty())
            this.completeRow();

        super.sendKeyboard(chatId, text);
    }
}