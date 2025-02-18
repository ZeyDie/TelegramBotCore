package com.zeydie.telegrambot.core.utils;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

@Log4j2
public final class LoggerUtil {
    public static void info(
            @NonNull final String message,
            @Nullable final Object... args
    ) {
        log.info(message, args);
    }

    public static void debug(
            @NonNull final String message,
            @Nullable final Object... args
    ) {
        log.debug(message, args);
    }

    public static void warn(
            @NonNull final String message,
            @Nullable final Object... args
    ) {
        log.warn(message, args);
    }

    public static void error(
            @NonNull final String message,
            @Nullable final Object... args
    ) {
        log.error(message, args);
    }

    public static void error(@NonNull final Exception exception) {
        log.error(exception);
        exception.printStackTrace();
    }
}