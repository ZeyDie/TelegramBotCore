package com.zeydie.telegrambot.core.configs.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public final class LanguageConfig {
    @NonFinal
    private boolean multiLanguage = false;
    @NonFinal
    private @NotNull String defaultLanguageId = "en";
    @NonFinal
    private boolean enableLanguageSelector = true;
}