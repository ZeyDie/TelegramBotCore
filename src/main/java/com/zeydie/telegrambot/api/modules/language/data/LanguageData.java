package com.zeydie.telegrambot.api.modules.language.data;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Data
public class LanguageData {
    @NonFinal
    private @NonNull String label;
    @NonFinal
    private @NonNull String uniqueId;
    @NonFinal
    private @NonNull Map<String, String> localization = Maps.newHashMap();

    public LanguageData(
            @NonNull final String label,
            @NonNull final String uniqueId
    ) {
        this(label, uniqueId, null);
    }

    public LanguageData(
            @NonNull final String label,
            @NonNull final String uniqueId,
            @Nullable final Map<String, String> localization
    ) {
        this.label = label;
        this.uniqueId = uniqueId;

        if (this.localization.isEmpty()) {
            this.localization.put("messages.reloaded", "Bot was reloaded!");
            this.localization.put("messages.select_language", "Select a language");
            this.localization.put("messages.changed_language", "Language was changed!");
            this.localization.put("messages.no_user_data", "Can't load user data!");
            this.localization.put("messages.donate.title", "Donate to Bot");
            this.localization.put("messages.donate.description", "Your support is very important!");

            if (localization != null && !localization.isEmpty())
                this.localization.putAll(localization);
        }
    }

    public @NotNull String localize(@NonNull final String key) {
        return this.localization.getOrDefault(key, key);
    }
}