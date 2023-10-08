package com.zeydie.telegrambot.api.listeners.impl;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.TelegramBotApp;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Log4j2
public class UpdatesListenerImpl implements UpdatesListener {
    @Override
    public int process(@NotNull final List<Update> updates) {
        final int status = TelegramBotApp.getStatus().isUpdatingMessages() ? CONFIRMED_UPDATES_ALL : CONFIRMED_UPDATES_NONE;

        if (status == CONFIRMED_UPDATES_ALL) {
            updates.forEach(update -> {
                log.info(update.toString());

                final Message message = update.message();

                if (message != null)
                    if (
                            TelegramBotApp.isChatingOlyUsers() ?
                                    (message.from().isBot() ? false : true) :
                                    true
                    )
                        TelegramBotApp.getMessagesCache().put(update.message());
            });
        }

        return status;
    }
}