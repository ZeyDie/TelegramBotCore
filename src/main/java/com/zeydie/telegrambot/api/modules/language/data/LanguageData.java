package com.zeydie.telegrambot.api.modules.language.data;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record LanguageData(String label, String uniqueId, @Nullable Map<String, String> localization) {
    public LanguageData {
        if (localization == null) {
            localization = new HashMap<>();
            localization.put("messages.select_language", "Select a language");
        }
    }
}