package com.zeydie.telegrambot.test;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.EventSubscribe;
import com.zeydie.telegrambot.api.events.PrioritySubscribe;
import com.zeydie.telegrambot.api.events.callback.CallbackQueryEvent;
import com.zeydie.telegrambot.api.events.callback.CallbackQuerySubscribe;
import com.zeydie.telegrambot.api.events.update.UpdateEvent;
import com.zeydie.telegrambot.api.events.update.UpdateSubscribe;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@EventSubscribe(comment = "Test callback class")
public class TestEvents {
    @CallbackQuerySubscribe(callbackDatas = {"en", "ru"}, comment = "Select a language")
    public void selectLanguage(@NotNull final CallbackQueryEvent callbackQueryEvent) {
        final CallbackQuery callbackQuery = callbackQueryEvent.getCallbackQuery();

        TelegramBotApp.getUserCache()
                .getUserData(callbackQuery.from())
                .setLanguageUniqueId(callbackQuery.data());
        TelegramBotApp.getUserCache().save();
    }

    @PrioritySubscribe(priority = EventPriority.LOWEST)
    @UpdateSubscribe
    public void lowest(@NotNull final UpdateEvent updateEvent) {
        log.debug("lowest {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.LOW)
    @UpdateSubscribe
    public void low(@NotNull final UpdateEvent updateEvent) {
        log.debug("low {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.DEFAULT)
    @UpdateSubscribe
    public void defaultA(@NotNull final UpdateEvent updateEvent) {
        log.debug("defaultA {}", updateEvent);
    }

    @UpdateSubscribe
    public void defaultB(@NotNull final UpdateEvent updateEvent) {
        log.debug("defaultB {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHT)
    @UpdateSubscribe
    public void hight(@NotNull final UpdateEvent updateEvent) {
        log.debug("hight {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHEST)
    @UpdateSubscribe
    public void highest(@NotNull final UpdateEvent updateEvent) {
        log.debug("highest {}", updateEvent);
    }
}
