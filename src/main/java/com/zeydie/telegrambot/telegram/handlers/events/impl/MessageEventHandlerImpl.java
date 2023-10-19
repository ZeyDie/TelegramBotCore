package com.zeydie.telegrambot.telegram.handlers.events.impl;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.TelegramBotApp;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.telegram.events.MessageEventSubscribe;
import com.zeydie.telegrambot.api.telegram.handlers.events.IMessageEventHandler;
import com.zeydie.telegrambot.telegram.events.MessageEvent;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;

@Log4j2
public class MessageEventHandlerImpl extends AbstractEventHandler implements IMessageEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return MessageEventSubscribe.class;
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
    public void handle(@NotNull final MessageData messageData) {
        this.handle(messageData.message());
    }

    @Override
    public void handle(@NotNull final Message message) {
        Arrays.stream(message.entities())
                .forEach(messageEntity -> {
                            switch (messageEntity.type()) {
                                case bot_command -> TelegramBotApp.getCommandEventHandler().handle(message);
                            }
                        }
                );

        super.invoke(new MessageEvent(message));
    }
}