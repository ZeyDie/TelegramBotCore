package com.zeydie.telegrambot.api.configs;

import com.zeydie.sgson.SGsonFile;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@RequiredArgsConstructor
public final class AbstractFileConfig {
    public static final Path CONFIG_SERVER_FOLDER = Paths.get("configs", "server");
    public static final Path CACHE_FOLDER = Paths.get("cache");

    @NotNull
    private final Class<?> parent;
    @NotNull
    private final String name;

    public @NotNull <T> T init() {
        return (T) new SGsonFile(CONFIG_SERVER_FOLDER.resolve(this.getName())).fromJsonToObject(this.parent);
    }
}
