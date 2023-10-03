package com.zeydie.telegrambot.api.modules.language.impl;

import com.zeydie.telegrambot.api.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Log
public final class MultiLanguage extends SingleLanguage {
    private final List<LanguageData> registeredLanguage = new ArrayList<>();

    @Override
    public void register(@NotNull final LanguageData languageData) throws Exception {
        final String label = languageData.getLabel();
        final String uniqueId = languageData.getUniqueId();

        if (isRegistered(languageData))
            throw new LanguageRegisteredException(uniqueId, label);

        this.registeredLanguage.add(initLangFile(languageData));

        this.log.info(
                String.format(
                        "%s (%s) was registerd!",
                        label,
                        uniqueId
                )
        );
    }

    @Override
    public boolean isRegistered(@NotNull final LanguageData languageData) {
        return isRegistered(languageData.getUniqueId());
    }

    @Override
    public boolean isRegistered(@NotNull final String uniqueId) {
        return this.registeredLanguage.stream()
                .anyMatch(languageData -> languageData.getUniqueId().equals(uniqueId));
    }

    @Override
    public @Nullable LanguageData getLanguageData(@NotNull final LanguageData languageData) {
        return getLanguageData(languageData.getUniqueId());
    }

    @Override
    public @Nullable LanguageData getLanguageData(@NotNull final String uniqueId) {
        return this.registeredLanguage.stream()
                .filter(languageData -> languageData.getUniqueId().equals(uniqueId))
                .findFirst()
                .orElse(null);
    }
}
