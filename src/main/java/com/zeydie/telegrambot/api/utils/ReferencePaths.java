package com.zeydie.telegrambot.api.utils;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class ReferencePaths {
    public static final @NotNull Path CONFIGS_FOLDER = Paths.get("configs");

    public static final @NotNull Path CACHE_FOLDER = Paths.get("cache");
    public static final @NotNull Path CACHE_MESSAGES_FOLDER = CACHE_FOLDER.resolve("messages");
    public static final @NotNull Path CACHE_USERS_FOLDER = CACHE_FOLDER.resolve("users");

    public static final @NotNull Path LANGUAGE_FOLDER = Paths.get("language");
}