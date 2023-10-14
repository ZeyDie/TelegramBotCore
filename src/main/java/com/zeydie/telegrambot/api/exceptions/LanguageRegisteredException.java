package com.zeydie.telegrambot.api.exceptions;

import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class LanguageRegisteredException extends Exception {
    @Getter
    private final @NotNull String message;

    public LanguageRegisteredException(@NotNull final LanguageData languageData) {
        this(languageData.uniqueId(), languageData.label());
    }

    public LanguageRegisteredException(
            @NotNull final String uniqueId,
            @NotNull final String label
    ) {
        this.message = String.format("%s (%s) is already registered!", label, uniqueId);
    }
}