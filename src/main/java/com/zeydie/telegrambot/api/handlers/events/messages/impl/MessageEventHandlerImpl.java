package com.zeydie.telegrambot.api.handlers.events.messages.impl;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.api.events.message.MessageEvent;
import com.zeydie.telegrambot.api.events.message.MessageSubscribe;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.handlers.events.messages.IMessageEventHandler;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

@Log4j2
public class MessageEventHandlerImpl extends AbstractEventHandler implements IMessageEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return MessageSubscribe.class;
    }

    @Override
    public @Nullable Class<?>[] getParameters() {
        return Collections.singleton(MessageEvent.class).toArray(new Class[]{});
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void handle(@NotNull final Message message) {
        super.invoke(new MessageEvent(message));
    }
}