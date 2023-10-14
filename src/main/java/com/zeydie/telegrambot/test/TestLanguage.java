package com.zeydie.telegrambot.test;

import com.zeydie.telegrambot.api.events.language.LanguageRegisterEvent;
import com.zeydie.telegrambot.api.events.language.LanguageRegisterEventSubscribe;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@EventSubscribesRegister(comment = "Test language class")
public final class TestLanguage {
    @SneakyThrows
    @LanguageRegisterEventSubscribe
    public void language(@NotNull final LanguageRegisterEvent event) {
        event.getLanguageRegister().register(
                new LanguageData(
                        "English",
                        "en",
                        Map.of(
                                "messages.welcome", "Welcome!",
                                "messages.select_language",  "Select a language"
                        )
                )
        );
        event.getLanguageRegister().register(
                new LanguageData(
                        "Russian",
                        "ru",
                        null
                )
        );
    }
}