package com.zeydie.telegrambot.configs;

import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.utils.FileUtil;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static com.zeydie.telegrambot.utils.ReferencePaths.CONFIG_TYPE;

public record AbstractFileConfig(@NonNull Path path, @NonNull Object parent, @NonNull String name) {
    public @NotNull <T> T init() {
        return (T) new SGsonFile(
                this.path.resolve(
                        FileUtil.createFileNameWithType(
                                this.name,
                                CONFIG_TYPE
                        )
                )
        ).fromJsonToObject(this.parent);
    }
}