package com.zeydie.telegrambot.core.utils;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public final class LanguageUtil {
    @Autowired
    private static ILanguage language;

    @SneakyThrows
    public static @NotNull String localize(@NonNull final String key) {
        return language.localize(key);
    }

    @SneakyThrows
    public static @NotNull String localize(
            @NonNull final UserData userData,
            @NonNull final String key
    ) {
        return language.localize(userData, key);
    }

    @SneakyThrows
    public static @NotNull String localize(
            @NonNull final User user,
            @NonNull final String key
    ) {
        return language.localize(user, key);
    }

    @SneakyThrows
    public static @NotNull String localize(
            final long userId,
            @NonNull final String key
    ) {
        return language.localize(userId, key);
    }
}