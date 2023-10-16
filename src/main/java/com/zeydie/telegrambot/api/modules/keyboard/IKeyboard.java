package com.zeydie.telegrambot.api.modules.keyboard;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import org.jetbrains.annotations.NotNull;

public interface IKeyboard {
    @NotNull
    Keyboard getKeyboard();

    void sendKeyboard(
            @NotNull final UserData user,
            @NotNull final String text
    );

    void sendKeyboard(
            @NotNull final User user,
            @NotNull final String text
    );

    void sendKeyboard(
            final long chatId,
            @NotNull final String text
    );
}