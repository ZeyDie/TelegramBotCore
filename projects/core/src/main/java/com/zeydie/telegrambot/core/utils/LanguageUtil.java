package com.zeydie.telegrambot.core.utils;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.core.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public final class LanguageUtil {
    @SneakyThrows
    public static @NotNull String localize(@NonNull final String key) {
        return TelegramBotCore.getInstance().getLanguage().localize(key);
    }

    @SneakyThrows
    public static @NotNull String localize(
            @NonNull final UserData userData,
            @NonNull final String key
    ) {
        return TelegramBotCore.getInstance().getLanguage().localize(userData, key);
    }

    @SneakyThrows
    public static @NotNull String localize(
            @NonNull final User user,
            @NonNull final String key
    ) {
        return TelegramBotCore.getInstance().getLanguage().localize(user, key);
    }

    @SneakyThrows
    public static @NotNull String localize(
            final long userId,
            @NonNull final String key
    ) {
        return TelegramBotCore.getInstance().getLanguage().localize(userId, key);
    }
}