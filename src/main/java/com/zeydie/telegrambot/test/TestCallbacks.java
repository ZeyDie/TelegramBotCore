package com.zeydie.telegrambot.test;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.subscribes.PrioritySubscribe;
import com.zeydie.telegrambot.api.telegram.events.callback.CallbackQueryEvent;
import com.zeydie.telegrambot.api.telegram.events.callback.CallbackQueryEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@EventSubscribesRegister(comment = "Test callback class")
public final class TestCallbacks {
    @PrioritySubscribe(priority = EventPriority.HIGHEST)
    @CallbackQueryEventSubscribe(callbackDatas = {"en", "ru"}, comment = "Select a language")
    public void selectLanguage(@NotNull final CallbackQueryEvent callbackQueryEvent) {
        final CallbackQuery callbackQuery = callbackQueryEvent.getCallbackQuery();

        TelegramBotApp.getUserCache()
                .getUserData(callbackQuery.from())
                .setLanguageUniqueId(callbackQuery.data());
        TelegramBotApp.getUserCache().save();
    }
}