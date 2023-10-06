package com.zeydie.telegrambot.api.modules.language.data;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record LanguageData(@NotNull String label, @NotNull String uniqueId, @NotNull Map<String, String> localization) {
    public LanguageData {
        if (localization == null)
            localization = new HashMap<>();
    }
}