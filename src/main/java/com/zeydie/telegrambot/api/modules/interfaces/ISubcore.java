package com.zeydie.telegrambot.api.modules.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISubcore extends IInitialize {
    @NotNull
    String getName();

    void launch(@Nullable final String[] args);

    void stop();
}