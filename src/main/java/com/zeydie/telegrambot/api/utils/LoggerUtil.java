package com.zeydie.telegrambot.api.utils;

import com.zeydie.telegrambot.configs.ConfigStore;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

@Log4j2
public final class LoggerUtil {
    public static void info(
            @NonNull String message,
            @Nullable final Object... args
    ) {
        info(null, message, args);
    }

    public static void info(
            @Nullable final Class<?> clazz,
            @NonNull String message,
            @Nullable final Object... args
    ) {
        if (clazz != null)
            message = clazz.getName() + " " + message;

        log.info(message, args);
    }

    public static void debug(
            @NonNull String message,
            @Nullable final Object... args
    ) {
        debug(null, message, args);
    }

    public static void debug(
            @Nullable final Class<?> clazz,
            @NonNull String message,
            @Nullable final Object... args
    ) {
        if (!ConfigStore.getBotConfig().isDebug()) return;
        if (clazz != null)
            message = clazz.getName() + " " + message;

        log.debug(message, args);
    }

    public static void warn(
            @NonNull String message,
            @Nullable final Object... args
    ) {
        warn(null, message, args);
    }

    public static void warn(
            @Nullable final Class<?> clazz,
            @NonNull String message,
            @Nullable final Object... args
    ) {
        if (clazz != null)
            message = clazz.getName() + " " + message;

        log.warn(message, args);
    }

    public static void error(
            @NonNull String message,
            @Nullable final Object... args
    ) {
        error(null, message, args);
    }

    public static void error(
            @Nullable final Class<?> clazz,
            @NonNull String message,
            @Nullable final Object... args
    ) {
        if (clazz != null)
            message = clazz.getName() + " " + message;

        log.error(message, args);
    }
}