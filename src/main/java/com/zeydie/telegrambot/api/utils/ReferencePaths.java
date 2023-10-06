package com.zeydie.telegrambot.api.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class ReferencePaths {
    public static final Path CONFIGS_FOLDER = Paths.get("configs");

    public static final Path CACHE_FOLDER = Paths.get("cache");
    public static final Path CACHE_MESSAGES_FOLDER = CACHE_FOLDER.resolve("messages");
    public static final Path CACHE_USERS_FOLDER = CACHE_FOLDER.resolve("users");

    public static final Path LANGUAGE_FOLDER = Paths.get("language");
}