package com.zeydie.telegrambot.api.configs;

import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.utils.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public record AbstractFileConfig(@NotNull Path path, @NotNull Object parent, @NotNull String name) {
    public @NotNull <T> T init() {
        return (T) new SGsonFile(
                path.resolve(
                        FileUtil.createFileWithType(
                                this.name,
                                "json"
                        )
                )
        ).fromJsonToObject(this.parent);
    }
}