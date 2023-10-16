package com.zeydie.telegrambot.exceptions;

import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class LanguageNotRegisteredException extends Exception{
    @Getter
    private final @NotNull String message;

    public LanguageNotRegisteredException(@NotNull final LanguageData languageData) {
        this(languageData.uniqueId(), languageData.label());
    }

    public LanguageNotRegisteredException(
            @NotNull final String uniqueId,
            @NotNull final String label
    ) {
        this.message = String.format("%s (%s) is not registered!", label, uniqueId);
    }
}
