package com.zeydie.telegrambot.api.events.config;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Category {
    CONFIGS,
    CACHE,
    LANGUAGE,
    PAYMENT,
    PERMISSIONS;

    @Override
    public @NotNull String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}