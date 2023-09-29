package com.zeydie.telegrambot.api.configs;

import com.zeydie.sgson.SGsonFile;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@RequiredArgsConstructor
public abstract class AbstractFileConfig {
    public static final Path CONFIG_SERVER_FOLDER = Paths.get("configs", "server");
    public static final Path CACHE_FOLDER = Paths.get("cache");

    private final Class<?> parent;
    private final String name;

    public Class<?> init() {
        return new SGsonFile(CONFIG_SERVER_FOLDER.resolve(this.getName())).fromJsonToObject(this.parent);
    }
}
