package com.zeydie.telegrambot.listeners.impl;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.TelegramBotCore;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Log4j2
public class UpdatesListenerImpl implements UpdatesListener {
    @Override
    public int process(@NotNull final List<Update> updates) {
        final int status = TelegramBotCore.getInstance().getStatus().isUpdatingMessages() ? CONFIRMED_UPDATES_ALL : CONFIRMED_UPDATES_NONE;

        if (status == CONFIRMED_UPDATES_ALL) {
            updates.forEach(update -> {
                        log.debug(update.toString());
                        TelegramBotCore.getInstance().getUpdateEventHandler().handle(update);
                    }
            );
        }

        return status;
    }
}