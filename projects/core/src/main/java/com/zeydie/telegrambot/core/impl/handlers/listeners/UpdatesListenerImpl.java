package com.zeydie.telegrambot.core.impl.handlers.listeners;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegrambot.api.Status;
import com.zeydie.telegrambot.api.telegram.events.handlers.IUpdateEventHandler;
import com.zeydie.telegrambot.core.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdatesListenerImpl implements UpdatesListener {
    @Autowired
    private Status status;
    @Autowired
    private IUpdateEventHandler updateEventHandler;

    @Override
    public int process(@NonNull final List<Update> updates) {
        val status = this.status.isUpdatingMessages() ? CONFIRMED_UPDATES_ALL : CONFIRMED_UPDATES_NONE;

        if (status == CONFIRMED_UPDATES_ALL)
            updates.forEach(
                    update -> {
                        LoggerUtil.debug(
                                SGsonBase.create()
                                        .setPretty()
                                        .fromObjectToJson(update)
                        );
                        this.updateEventHandler.handle(update);
                    }
            );

        return status;
    }
}