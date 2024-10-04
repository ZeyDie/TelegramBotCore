package com.zeydie.telegrambot.api.modules.language;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.exceptions.language.LanguageNotRegisteredException;
import com.zeydie.telegrambot.exceptions.language.LanguageRegisteredException;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ILanguage extends IInitialize {
    boolean register(@NonNull final LanguageData languageData) throws LanguageRegisteredException;

    @NotNull
    List<LanguageData> getRegisteredLanguages();

    boolean isRegistered(@NonNull final LanguageData languageData);

    boolean isRegistered(@NonNull final String uniqueId);

    @Nullable
    LanguageData getLanguageData(@NonNull final LanguageData languageData);

    @Nullable
    LanguageData getLanguageData(@NonNull final String uniqueId);

    @NotNull
    String localizeObject(
            @Nullable final Object object,
            @NonNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(
            @Nullable final UserData userData,
            @NonNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(
            @Nullable final User user,
            @NonNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(
            final long id,
            @NonNull final String key
    ) throws LanguageNotRegisteredException;

    @NotNull
    String localize(@NonNull final String key) throws LanguageNotRegisteredException;
}