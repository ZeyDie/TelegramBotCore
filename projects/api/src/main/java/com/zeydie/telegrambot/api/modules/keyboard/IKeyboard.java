package com.zeydie.telegrambot.api.modules.keyboard;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface IKeyboard {
    @NotNull
    Keyboard getKeyboard();

    void sendKeyboard(@NonNull final UserData userData);

    void sendKeyboard(@NonNull final User user);

    void sendKeyboard(final long chatId);

    void sendKeyboard(
            @NonNull final UserData userData,
            @NonNull final String text
    );

    void sendKeyboard(
            @NonNull final User user,
            @NonNull final String text
    );

    void sendKeyboard(
            final long chatId,
            @NonNull final String text
    );
}