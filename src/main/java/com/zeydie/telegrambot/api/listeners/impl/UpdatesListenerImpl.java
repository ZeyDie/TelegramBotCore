package com.zeydie.telegrambot.api.listeners.impl;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.Application;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Log
public class UpdatesListenerImpl implements UpdatesListener {
    @Override
    public int process(@NotNull final List<Update> updates) {
        final int status = Application.getBot().getStatus().isUpdatingMessages() ? CONFIRMED_UPDATES_ALL : CONFIRMED_UPDATES_NONE;

        if (status == CONFIRMED_UPDATES_ALL) {
            updates.forEach(update -> {
                log.info(update.toString());

                Application.getBot().getCacheUpdates().put(update.message());
            });
        }

        return status;
    }
}
