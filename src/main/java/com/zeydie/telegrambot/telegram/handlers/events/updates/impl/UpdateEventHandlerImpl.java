package com.zeydie.telegrambot.telegram.handlers.events.updates.impl;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.TelegramBotApp;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.telegram.events.update.UpdateEventSubscribe;
import com.zeydie.telegrambot.api.telegram.handlers.events.updates.IUpdateEventHandler;
import com.zeydie.telegrambot.telegram.events.update.UpdateEvent;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

@Log4j2
public class UpdateEventHandlerImpl extends AbstractEventHandler implements IUpdateEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return UpdateEventSubscribe.class;
    }

    @Override
    public @Nullable Class<?>[] getParameters() {
        return Collections.singleton(UpdateEvent.class).toArray(new Class[]{});
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void handle(@NotNull final Update update) {
        @NotNull final UpdateEvent updateEvent = new UpdateEvent(update);

        super.invoke(updateEvent);

        if (!updateEvent.isCancelled()) {
            @NotNull final CallbackQuery callbackQuery = update.callbackQuery();

            if (callbackQuery != null)
                TelegramBotApp.getCallbackQueryHandler().handle(callbackQuery);

            @Nullable final Message message = update.message();

            if (message != null)
                TelegramBotApp.getMessagesCache().put(new MessageData(message));
        }
    }
}