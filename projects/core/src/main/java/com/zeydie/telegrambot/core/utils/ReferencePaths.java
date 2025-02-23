package com.zeydie.telegrambot.core.utils;

import com.zeydie.telegrambot.api.events.config.Category;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ReferencePaths {
    public static final @NotNull Path CONFIGS_FOLDER_PATH = Paths.get(Category.CONFIGS.toString());

    public static final @NotNull Path CACHE_FOLDER_PATH = Paths.get(Category.CACHE.toString());
    public static final @NotNull Path CACHE_MESSAGES_FOLDER_PATH = CACHE_FOLDER_PATH.resolve("messages");
    public static final @NotNull File CACHE_MESSAGES_FOLDER_FILE = CACHE_MESSAGES_FOLDER_PATH.toFile();
    public static final @NotNull Path CACHE_USERS_FOLDER_PATH = CACHE_FOLDER_PATH.resolve("users");
    public static final @NotNull File CACHE_USERS_FOLDER_FILE = CACHE_USERS_FOLDER_PATH.toFile();

    public static final @NotNull Path LANGUAGE_FOLDER_PATH = Paths.get(Category.LANGUAGE.toString());
    public static final @NotNull File LANGUAGE_FOLDER_FILE = LANGUAGE_FOLDER_PATH.toFile();

    public static final @NotNull Path PAYMENT_FOLDER_PATH = Paths.get(Category.PAYMENT.toString());
    public static final @NotNull File PAYMENT_FOLDER_FILE = PAYMENT_FOLDER_PATH.toFile();
    public static final @NotNull String TELEGRAM_CURRENCY_URL = "https://core.telegram.org/bots/payments/currencies.json";

    public static final @NotNull Path PERMISSIONS_FOLDER_PATH = Paths.get(Category.PERMISSIONS.toString());
    public static final @NotNull File PERMISSIONS_FOLDER_FILE = PERMISSIONS_FOLDER_PATH.toFile();

    public static @NotNull String CONFIG_TYPE = "cfg";
    public static @NotNull String LANGUAGE_TYPE = "lang";
    public static @NotNull String DATA_TYPE = "data";
    public static @NotNull String PERMISSION_TYPE = "pex";
}