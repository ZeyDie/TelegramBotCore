package com.zeydie.telegrambot.api.configs;

import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.utils.FileUtil;
import org.jetbrains.annotations.NotNull;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.CONFIGS_FOLDER;

public record AbstractFileConfig(@NotNull Object parent, @NotNull String name) {
    public @NotNull <T> T init() {
        return (T) new SGsonFile(
                CONFIGS_FOLDER.resolve(
                        FileUtil.createFileWithType(
                                this.name,
                                "json"
                        )
                )
        ).fromJsonToObject(this.parent);
    }
}
