package com.zeydie.telegrambot.api.telegram.handlers.events;

import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.modules.interfaces.ILoading;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface IUpdateEventHandler extends ILoading {
    void handle(@NonNull final Update update);
}