package com.zeydie.telegrambot.api.modules.keyboard;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import org.jetbrains.annotations.NotNull;

public interface IMessageKeyboard extends IKeyboard {
    @NotNull
    IMessageKeyboard addButton(@NotNull final InlineKeyboardButton keyboardButton);

    @NotNull
    IMessageKeyboard completeRow();
}