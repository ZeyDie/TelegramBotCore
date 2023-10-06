package com.zeydie.telegrambot.api.modules.language;

import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ILanguage {
    void register(@NotNull final LanguageData languageData) throws Exception;

    @NotNull
    List<LanguageData> getRegisteredLanguages();

    boolean isRegistered(@NotNull final LanguageData languageData);

    boolean isRegistered(@NotNull final String uniqueId);

    @Nullable
    LanguageData getLanguageData(@NotNull final LanguageData languageData);

    @Nullable
    LanguageData getLanguageData(@NotNull final String uniqueId);
}