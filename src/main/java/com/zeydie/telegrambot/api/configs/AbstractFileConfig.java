package com.zeydie.telegrambot.api.configs;

import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.utils.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public record AbstractFileConfig(@NotNull Object parent, @NotNull String name) {
    public static final Path CONFIGS_FOLDER = Paths.get("configs");

    public static final Path CACHE_FOLDER = Paths.get("cache");
    public static final Path CACHE_MESSAGES_FOLDER = CACHE_FOLDER.resolve("messages");
    public static final Path CACHE_USERS_FOLDER = CACHE_FOLDER.resolve("users");

    public @NotNull <T> T init() {
        return (T) new SGsonFile(CONFIGS_FOLDER.resolve(FileUtil.createFileWithType(this.name, "json"))).fromJsonToObject(this.parent);
    }
}
