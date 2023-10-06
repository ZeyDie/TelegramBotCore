package com.zeydie.telegrambot.api.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class LanguageRegisteredException extends Exception {
    @Getter
    private final String message;

    public LanguageRegisteredException(
            @NotNull final String uniqueId,
            @NotNull final String label
    ) {
        this.message = String.format("%s (%s) is already registered!", label, uniqueId);
    }
}