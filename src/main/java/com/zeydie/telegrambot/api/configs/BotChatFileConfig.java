package com.zeydie.telegrambot.api.configs;

import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.NonFinal;

import java.util.HashMap;

public final class BotChatFileConfig {
    @Getter
    private static Json json = new AbstractFileConfig(new Json(), "bot_chat").init();

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Json {
        @NonFinal
        private boolean caching = true;
        @NonFinal
        private boolean chatingOlyUsers = true;
        @NonFinal
        private boolean multiLanguage = true;
        @NonFinal
        private LanguageData defaultLanguageData = new LanguageData("English", "en", new HashMap<>());
    }
}