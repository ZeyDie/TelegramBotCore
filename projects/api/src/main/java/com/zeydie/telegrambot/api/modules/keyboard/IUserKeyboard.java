package com.zeydie.telegrambot.api.modules.keyboard;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface IUserKeyboard extends IKeyboard {
    @NotNull
    IUserKeyboard addButton(@NonNull final KeyboardButton keyboardButton);

    @NotNull
    IUserKeyboard completeRow();
}