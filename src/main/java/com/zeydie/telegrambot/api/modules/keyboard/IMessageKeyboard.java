package com.zeydie.telegrambot.api.modules.keyboard;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface IMessageKeyboard extends IKeyboard {
    @NotNull
    IMessageKeyboard addButton(@NonNull final InlineKeyboardButton keyboardButton);

    @NotNull
    IMessageKeyboard completeRow();
}