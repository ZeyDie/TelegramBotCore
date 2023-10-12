package com.zeydie.telegrambot.api.handlers.events.callbacks.impl;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.events.callback.CallbackQueryEvent;
import com.zeydie.telegrambot.api.events.callback.CallbackQuerySubscribe;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.handlers.events.callbacks.ICallbackQueryEventHandler;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

@Log4j2
public class CallbackQueryEventHandlerImpl extends AbstractEventHandler implements ICallbackQueryEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return CallbackQuerySubscribe.class;
    }

    @Override
    public @Nullable Class<?>[] getParameters() {
        return Collections.singleton(CallbackQueryEvent.class).toArray(new Class[]{});
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void handle(@NotNull final CallbackQuery callbackQuery) {
        super.invoke(new CallbackQueryEvent(callbackQuery));
    }
}