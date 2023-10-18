package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.Update;
import org.jetbrains.annotations.NotNull;

public interface IUpdateEventHandler {
    void load();

    void handle(@NotNull final Update update);
}