package com.zeydie.telegrambot.api.configs;

import com.zeydie.sgson.SGsonFile;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public record AbstractFileConfig(@NotNull Class<?> parent, @NotNull String name) {
    public static final Path CONFIG_SERVER_FOLDER = Paths.get("configs", "server");
    public static final Path CACHE_FOLDER = Paths.get("cache");

    public @NotNull <T> T init() {
        return (T) new SGsonFile(CONFIG_SERVER_FOLDER.resolve(this.name)).fromJsonToObject(this.parent);
    }
}
