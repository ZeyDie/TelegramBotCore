package com.zeydie.telegrambot.api.modules.language.data;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record LanguageData(String label, String uniqueId, @Nullable Map<String, String> localization) {
    public LanguageData {
        if (localization == null) {
            localization = new HashMap<>();
            localization.put("messages.reloaded", "Bot was reloaded!");
            localization.put("messages.select_language", "Select a language");
            localization.put("messages.changed_language", "Language was changed!");
        }
    }

    public @NotNull String localize(@NonNull final String key) {
        assert this.localization != null;

        return this.localization.getOrDefault(key, key);
    }
}