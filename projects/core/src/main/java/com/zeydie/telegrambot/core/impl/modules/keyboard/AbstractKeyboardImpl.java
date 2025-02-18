package com.zeydie.telegrambot.core.impl.modules.keyboard;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.keyboard.IKeyboard;
import com.zeydie.telegrambot.core.utils.SendMessageUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class AbstractKeyboardImpl implements IKeyboard {
    private final @NotNull String text;

    @Override
    public void sendKeyboard(@NonNull final UserData userData) {
        this.sendKeyboard(userData, this.text);
    }

    @Override
    public void sendKeyboard(@NonNull final User user) {
        this.sendKeyboard(user, this.text);
    }


    @Override
    public void sendKeyboard(final long chatId) {
        this.sendKeyboard(chatId, this.text);
    }

    @Override
    public void sendKeyboard(
            @NonNull final UserData userData,
            @NonNull final String text
    ) {
        this.sendKeyboard(userData.getUser(), text);
    }

    @Override
    public void sendKeyboard(
            @NonNull final User user,
            @NonNull final String text
    ) {
        this.sendKeyboard(user.id(), text);
    }

    @Override
    public void sendKeyboard(
            final long chatId,
            @NonNull final String text
    ) {
        SendMessageUtil.execute(new SendMessage(chatId, text).replyMarkup(this.getKeyboard()));
    }
}