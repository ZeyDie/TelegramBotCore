package com.zeydie.telegrambot.test.launch;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.keyboard.IMessageKeyboard;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.modules.keyboard.impl.MessageKeyboardImpl;
import org.jetbrains.annotations.Nullable;

// TODO AnswerCallBackQuery

public final class StartApp {
    public static void main(@Nullable final String[] args) throws LanguageRegisteredException {
        TelegramBotApp.start();
        TelegramBotApp.setup();
        TelegramBotApp.init();

        final long chatId = 5099834947L;

        if (ConfigStore.getLanguageConfig().isMultiLanguage()) {
            final IMessageKeyboard messageKeyboard = new MessageKeyboardImpl();

            TelegramBotApp.getLanguage()
                    .getRegisteredLanguages()
                    .forEach(
                            languageData -> messageKeyboard.addButton(
                                    new InlineKeyboardButton(languageData.label())
                                            .callbackData(languageData.uniqueId())
                            )
                    );

            messageKeyboard.sendKeyboard(chatId, "messages.select_language");
        } else TelegramBotApp.execute(new SendMessage(chatId, "messages.welcome"));
    }
}