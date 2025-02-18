package com.zeydie.telegrambot.core.impl.handlers.events;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.handlers.ICommandEventHandler;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.core.impl.handlers.AbstractEventHandler;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Collections;

@Service
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
    public void handle(@NonNull final Message message) {
        super.invoke(new CommandEvent(message));
    }
}