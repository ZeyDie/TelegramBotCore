package com.zeydie.telegrambot.api.modules.keyboard.abstracted;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.keyboard.IKeyboard;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKeyboardImpl implements IKeyboard {
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
        TelegramBotCore.getInstance().execute(new SendMessage(chatId, text).replyMarkup(this.getKeyboard()));
    }
}