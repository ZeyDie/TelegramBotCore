package com.zeydie.telegrambot.listeners.impl;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;

import java.util.List;

public class UpdatesListenerImpl implements UpdatesListener {
    @Override
    public int process(@NonNull final List<Update> updates) {
        val status = TelegramBotCore.getInstance().getStatus().isUpdatingMessages() ? CONFIRMED_UPDATES_ALL : CONFIRMED_UPDATES_NONE;

        if (status == CONFIRMED_UPDATES_ALL)
            updates.forEach(
                    update -> {
                        LoggerUtil.debug(
                                this.getClass(),
                                SGsonBase.create()
                                        .setPretty()
                                        .fromObjectToJson(update)
                        );
                        TelegramBotCore.getInstance().getUpdateEventHandler().handle(update);
                    }
            );

        return status;
    }
}