package com.zeydie.telegrambot.core.impl.handlers.events;

import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.core.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.telegram.events.UpdateEvent;
import com.zeydie.telegrambot.api.telegram.events.handlers.IUpdateEventHandler;
import com.zeydie.telegrambot.api.telegram.events.subscribes.UpdateEventSubscribe;
import com.zeydie.telegrambot.core.impl.handlers.AbstractEventHandler;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;

public class UpdateEventHandlerImpl extends AbstractEventHandler implements IUpdateEventHandler {
    @Override
    public @NotNull Class<? extends Annotation> getEventAnnotation() {
        return UpdateEventSubscribe.class;
    }

    @Override
    public @Nullable Class<?>[] getParameters() {
        return Collections.singleton(UpdateEvent.class).toArray(new Class[]{});
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
    public void handle(@NonNull final Update update) {
        @NonNull val updateEvent = new UpdateEvent(update);

        super.invoke(updateEvent);

        if (!updateEvent.isCancelled()) {
            @NonNull val callbackQuery = update.callbackQuery();

            if (callbackQuery != null)
                TelegramBotCore.getInstance().getCallbackQueryEventHandler().handle(callbackQuery);

            @Nullable val message = update.message();

            if (message != null)
                TelegramBotCore.getInstance().getMessagesCache().put(new MessageData(message));
        }
    }
}