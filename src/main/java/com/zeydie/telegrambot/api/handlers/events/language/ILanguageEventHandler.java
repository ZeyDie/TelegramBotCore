package com.zeydie.telegrambot.api.handlers.events.language;

import com.zeydie.telegrambot.api.events.language.LanguageRegisterEvent;
import org.jetbrains.annotations.NotNull;

public interface ILanguageEventHandler {
    void load();

    void handle(@NotNull final LanguageRegisterEvent languageRegisterEvent);
}