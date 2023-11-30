package com.zeydie.telegrambot.exceptions;

import com.zeydie.telegrambot.api.modules.interfaces.ISubcore;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class SubcoreRegisteredException extends Exception {
    @Getter
    private final @NotNull String message;

    public SubcoreRegisteredException(@NonNull final ISubcore subcore) {
        this(subcore.getClass().getSimpleName(), subcore.getName());
    }

    public SubcoreRegisteredException(
            @NonNull final String uniqueId,
            @NonNull final String label
    ) {
        this.message = String.format("%s (%s) is already registered!", label, uniqueId);
    }
}