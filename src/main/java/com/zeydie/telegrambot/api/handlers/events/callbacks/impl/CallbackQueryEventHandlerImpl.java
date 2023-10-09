package com.zeydie.telegrambot.api.handlers.events.callbacks.impl;

import com.google.common.base.Predicate;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.zeydie.telegrambot.api.events.callback.CallbackQueryEvent;
import com.zeydie.telegrambot.api.events.callback.CallbackQuerySubscribe;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.handlers.events.callbacks.ICallbackQueryEventHandler;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;

@Log4j2
public class CallbackQueryEventHandlerImpl extends AbstractEventHandler implements ICallbackQueryEventHandler {
    @NotNull
    @Override
    public Class<? extends Annotation> getEventAnnotation() {
        return CallbackQuerySubscribe.class;
    }

    @Nullable
    @Override
    public Class<?>[] getParameters() {
        return Collections.singleton(CallbackQueryEvent.class).toArray(new Class[]{});
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void handle(@NotNull final CallbackQuery callbackQuery) {
        final CallbackQueryEvent callbackQueryEvent = new CallbackQueryEvent(callbackQuery);

        this.getClassMethods()
                .forEach((method, annotatedClass) -> {
                    final CallbackQuerySubscribe callbackQuerySubscribe = (CallbackQuerySubscribe) method.getAnnotation(this.getEventAnnotation());
                    final boolean invoke = callbackQuerySubscribe.ignoreCancelled() || !callbackQueryEvent.isCancelled();

                    if (
                            Arrays.stream(callbackQuerySubscribe.callbackDatas())
                                    .anyMatch((Predicate<String>) input -> input.equals(callbackQuery.data()))
                    )
                        if (invoke)
                            super.invoke(annotatedClass, method, callbackQueryEvent);
                });
    }
}
