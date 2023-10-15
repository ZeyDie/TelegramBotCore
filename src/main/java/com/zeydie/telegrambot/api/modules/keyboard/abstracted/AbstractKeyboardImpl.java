package com.zeydie.telegrambot.api.modules.keyboard.abstracted;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.keyboard.IKeyboard;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public abstract class AbstractKeyboardImpl implements IKeyboard {
    @Override
    public void sendKeyboard(
            @NotNull final User user,
            @NotNull final String text
    ) {
        this.sendKeyboard(user.id(), text);
    }

    @Override
    public void sendKeyboard(
            final long chatId,
            @NotNull final String text
    ) {
        TelegramBotApp.execute(new SendMessage(chatId, text).replyMarkup(this.getKeyboard()));
    }
}