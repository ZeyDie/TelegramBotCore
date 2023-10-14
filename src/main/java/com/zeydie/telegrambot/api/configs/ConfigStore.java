package com.zeydie.telegrambot.api.configs;

import com.zeydie.telegrambot.api.configs.data.BotConfig;
import com.zeydie.telegrambot.api.configs.data.CachingConfig;
import com.zeydie.telegrambot.api.configs.data.LanguageConfig;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@ConfigSubscribesRegister
public final class ConfigStore {
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "config")
    public static @NotNull BotConfig botConfig = new BotConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "language")
    public static @NotNull LanguageConfig languageConfig = new LanguageConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "caching")
    public static @NotNull CachingConfig cachingConfig = new CachingConfig();
}