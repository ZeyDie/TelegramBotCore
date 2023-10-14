package com.zeydie.telegrambot.api.utils;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public final class ReferencePaths {
    public static final @NotNull Path CONFIGS_FOLDER_PATH = Paths.get(Category.CONFIGS.toString());

    public static final @NotNull Path CACHE_FOLDER_PATH = Paths.get(Category.CACHE.toString());
    public static final @NotNull Path CACHE_MESSAGES_FOLDER_PATH = CACHE_FOLDER_PATH.resolve("messages");
    public static final @NotNull Path CACHE_USERS_FOLDER_PATH = CACHE_FOLDER_PATH.resolve("users");

    public static final @NotNull Path LANGUAGE_FOLDER_PATH = Paths.get(Category.LANGUAGE.toString());

    public static @NotNull String CONFIG_TYPE = "cfg";
    public static @NotNull String LANGUAGE_TYPE = "lang";
    public static @NotNull String DATA_TYPE = "data";

    public enum Category {
        CONFIGS, CACHE, LANGUAGE;

        @Override
        public @NotNull String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}