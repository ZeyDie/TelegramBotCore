package com.zeydie.telegrambot.api.configs.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.NonFinal;

@Data
@EqualsAndHashCode(callSuper = false)
public final class BotChatFileConfig {
    @NonFinal
    private boolean caching = true;
    @NonFinal
    private boolean multiLanguage = true;
    @NonFinal
    private String defaultLanguageId = "en";
}