package com.zeydie.telegrambot.api.modules.language.data;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public record LanguageData(String label, String uniqueId, @Nullable Map<String, String> localization) {
    public LanguageData {
        if (localization == null) {
            localization = new HashMap<>();
            localization.put("messages.select_language", "Select a language");
        }
    }

    public @NotNull String localize(@NotNull final String key) {
        assert this.localization != null;
        @NotNull final String localized = this.localization.getOrDefault(key, key);

        log.debug("[{}] localize {}={}", this.label, key, localized);

        return localized;
    }
}