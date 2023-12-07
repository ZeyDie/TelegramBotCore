package com.zeydie.telegrambot.handlers.events.language.impl;

import com.zeydie.telegrambot.api.events.language.LanguageRegisterEvent;
import com.zeydie.telegrambot.api.events.language.LanguageRegisterEventSubscribe;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.handlers.events.language.ILanguageEventHandler;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

public class LanguageEventHandlerImpl extends AbstractEventHandler implements ILanguageEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return LanguageRegisterEventSubscribe.class;
    }

    @Override
    public @Nullable Class<?>[] getParameters() {
        return Collections.singleton(LanguageRegisterEvent.class).toArray(new Class[]{});
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
    public void handle(@NonNull final LanguageRegisterEvent languageRegisterEvent) {
        super.invoke(languageRegisterEvent);
    }
}