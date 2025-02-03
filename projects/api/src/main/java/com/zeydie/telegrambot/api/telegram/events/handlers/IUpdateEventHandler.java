package com.zeydie.telegrambot.api.telegram.events.handlers;

import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import lombok.NonNull;

public interface IUpdateEventHandler extends IInitialize {
    void handle(@NonNull final Update update);
}