package com.zeydie.telegrambot.telegram.handlers.events.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.telegram.events.MessageEventSubscribe;
import com.zeydie.telegrambot.api.telegram.handlers.events.IMessageEventHandler;
import com.zeydie.telegrambot.telegram.events.MessageEvent;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
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
    public void handle(@NonNull final MessageData messageData) {
        this.handle(messageData.message());
    }

    @Override
    public void handle(@NonNull final Message message) {
        @Nullable val messageEntities = message.entities();

        if (messageEntities != null)
            Arrays.stream(messageEntities)
                    .forEach(messageEntity -> {
                                switch (messageEntity.type()) {
                                    case bot_command -> TelegramBotCore.getInstance().getCommandEventHandler().handle(message);
                                }
                            }
                    );

        super.invoke(new MessageEvent(message));
    }
}