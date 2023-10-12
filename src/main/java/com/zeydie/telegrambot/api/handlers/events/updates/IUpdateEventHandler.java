package com.zeydie.telegrambot.api.handlers.events.updates;

import com.pengrad.telegrambot.model.Update;
import org.jetbrains.annotations.NotNull;

public interface IUpdateEventHandler {
    void load();

    void handle(@NotNull final Update update);
}