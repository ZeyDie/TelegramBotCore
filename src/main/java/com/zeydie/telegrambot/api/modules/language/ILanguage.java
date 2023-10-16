package com.zeydie.telegrambot.api.modules.language;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.exceptions.LanguageNotRegisteredException;
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

    @NotNull
    String localize(
            @Nullable final Object object,
            @NotNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(
            @Nullable final UserData userData,
            @NotNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(
            @Nullable final User user,
            @NotNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(
            final long id,
            @NotNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(@NotNull final String key) throws LanguageNotRegisteredException;
}