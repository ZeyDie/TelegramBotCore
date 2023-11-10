package com.zeydie.telegrambot.telegram.handlers.events.impl;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.telegram.events.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.handlers.events.ICommandEventHandler;
import com.zeydie.telegrambot.telegram.events.CommandEvent;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

public class CommandEventHandlerImpl extends AbstractEventHandler implements ICommandEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return CommandEventSubscribe.class;
    }

    @Override
    public @Nullable Class<?>[] getParameters() {
        return Collections.singleton(CommandEvent.class).toArray(new Class[]{});
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void handle(@NonNull final Message message) {
        super.invoke(new CommandEvent(message));
    }
}