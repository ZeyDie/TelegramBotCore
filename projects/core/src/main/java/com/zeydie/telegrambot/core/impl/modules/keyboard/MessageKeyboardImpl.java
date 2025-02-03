package com.zeydie.telegrambot.core.impl.modules.keyboard;

import com.google.common.collect.Lists;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.zeydie.telegrambot.api.modules.keyboard.IMessageKeyboard;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageKeyboardImpl extends AbstractKeyboardImpl implements IMessageKeyboard {
    public MessageKeyboardImpl(@NotNull final String text) {
        super(text);
    }

    public static @NotNull MessageKeyboardImpl create(@NonNull final String text) {
        return new MessageKeyboardImpl(text);
    }

    private final @NotNull List<InlineKeyboardButton> rowKeyboardButtonList = Lists.newArrayList();

    @Getter
    private final @NotNull InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(this.rowKeyboardButtonList.toArray(new InlineKeyboardButton[]{}));

    @Override
    public @NotNull IMessageKeyboard addButton(@NonNull final InlineKeyboardButton keyboardButton) {
        this.rowKeyboardButtonList.add(keyboardButton);

        return this;
    }

    @Override
    public @NotNull IMessageKeyboard completeRow() {
        this.keyboard.addRow(this.rowKeyboardButtonList.toArray(new InlineKeyboardButton[]{}));
        this.rowKeyboardButtonList.clear();

        return this;
    }

    @Override
    public void sendKeyboard(final long chatId, @NonNull final String text) {
        if (!this.rowKeyboardButtonList.isEmpty())
            this.completeRow();

        super.sendKeyboard(chatId, text);
    }
}