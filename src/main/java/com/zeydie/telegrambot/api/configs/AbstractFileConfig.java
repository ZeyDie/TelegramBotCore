package com.zeydie.telegrambot.api.configs;

import com.zeydie.sgson.SGsonFile;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public record AbstractFileConfig(@NotNull Class<?> parent, @NotNull String name) {
    public static final Path CONFIGS_SERVER_FOLDER = Paths.get("configs", "server");
    public static final Path CACHE_FOLDER = Paths.get("cache");
    public static final Path CACHE_MESSAGES_FOLDER = CACHE_FOLDER.resolve("messages");
    public static final Path CACHE_USERS_FOLDER = CACHE_FOLDER.resolve("users");

    public @NotNull <T> T init() {
        return (T) new SGsonFile(CONFIGS_SERVER_FOLDER.resolve(this.name)).fromJsonToObject(this.parent);
    }
}
