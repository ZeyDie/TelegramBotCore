package com.zeydie.telegrambot.api.modules.language;

import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.exceptions.LanguageRegisteredException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ILanguage {
    void load() throws LanguageRegisteredException;

    boolean register(@NotNull final LanguageData languageData) throws LanguageRegisteredException;

    @NotNull
    List<LanguageData> getRegisteredLanguages();

    boolean isRegistered(@NotNull final LanguageData languageData);

    boolean isRegistered(@NotNull final String uniqueId);

    @Nullable
    LanguageData getLanguageData(@NotNull final LanguageData languageData);

    @Nullable
    LanguageData getLanguageData(@NotNull final String uniqueId);
}