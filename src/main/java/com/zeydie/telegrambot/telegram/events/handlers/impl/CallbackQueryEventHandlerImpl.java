package com.zeydie.telegrambot.telegram.events.handlers.impl;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.telegram.events.CallbackQueryEvent;
import com.zeydie.telegrambot.api.telegram.events.handlers.ICallbackQueryEventHandler;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CallbackQueryEventSubscribe;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

public class CallbackQueryEventHandlerImpl extends AbstractEventHandler implements ICallbackQueryEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return CallbackQueryEventSubscribe.class;
    }

    @Override
    public @Nullable Class<?>[] getParameters() {
        return Collections.singleton(CallbackQueryEvent.class).toArray(new Class[]{});
    }

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
    }

    @Override
    public void handle(@NonNull final CallbackQuery callbackQuery) {
        super.invoke(new CallbackQueryEvent(callbackQuery));
    }
}