package com.zeydie.telegrambot.api.handlers.events.updates.impl;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.TelegramBotApp;
import com.zeydie.telegrambot.api.events.update.UpdateEvent;
import com.zeydie.telegrambot.api.events.update.UpdateSubscribe;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.handlers.events.updates.IUpdateEventHandler;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

@Log4j2
public class UpdateEventHandlerImpl extends AbstractEventHandler implements IUpdateEventHandler {
    @NotNull
    @Override
    public Class<? extends Annotation> getEventAnnotation() {
        return UpdateSubscribe.class;
    }

    @Nullable
    @Override
    public Class<?>[] getParameters() {
        return Collections.singleton(UpdateEvent.class).toArray(new Class[]{});
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void handle(@NotNull final Update update) {
        final CallbackQuery callbackQuery = update.callbackQuery();

        if (callbackQuery != null)
            TelegramBotApp.getCallbackQueryHandler().handle(callbackQuery);

        final UpdateEvent updateEvent = new UpdateEvent(update);

        this.getClassMethods()
                .forEach((method, annotatedClass) -> {
                    final UpdateSubscribe updateSubscribe = (UpdateSubscribe) method.getAnnotation(this.getEventAnnotation());
                    final boolean invoke = updateSubscribe.ignoreCancelled() || !updateEvent.isCancelled();

                    if (invoke)
                        super.invoke(annotatedClass, method, updateEvent);
                });
    }
}
