package com.zeydie.telegrambot.api.handlers.events.language;

import com.zeydie.telegrambot.api.events.language.LanguageRegisterEvent;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import org.jetbrains.annotations.NotNull;

public interface ILanguageEventHandler extends IInitialize {
    void handle(@NotNull final LanguageRegisterEvent languageRegisterEvent);
}