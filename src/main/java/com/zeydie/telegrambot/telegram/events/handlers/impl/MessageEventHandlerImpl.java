package com.zeydie.telegrambot.telegram.events.handlers.impl;

import com.pengrad.telegrambot.model.Message;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.handlers.AbstractEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.telegram.events.MessageEvent;
import com.zeydie.telegrambot.api.telegram.events.handlers.IMessageEventHandler;
import com.zeydie.telegrambot.api.telegram.events.subscribes.MessageEventSubscribe;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;

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
        val message = messageData.message();

        if (message != null)
            this.handle(message);
    }

    @Override
    public void handle(@NonNull final Message message) {
        @Nullable val messageEntities = message.entities();

        if (messageEntities != null)
            Arrays.stream(messageEntities)
                    .forEach(messageEntity -> {
                                @Nullable val type = messageEntity.type();

                                if (type != null)
                                    switch (type) {
                                        case bot_command ->
                                                TelegramBotCore.getInstance().getCommandEventHandler().handle(message);
                                    }
                            }
                    );

        super.invoke(new MessageEvent(message));
    }
}