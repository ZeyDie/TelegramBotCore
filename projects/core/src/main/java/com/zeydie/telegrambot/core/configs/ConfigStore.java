package com.zeydie.telegrambot.core.configs;

import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import com.zeydie.telegrambot.core.configs.data.BotConfig;
import com.zeydie.telegrambot.core.configs.data.CachingConfig;
import com.zeydie.telegrambot.core.configs.data.DonateConfig;
import com.zeydie.telegrambot.core.configs.data.LanguageConfig;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@ConfigSubscribesRegister
public final class ConfigStore {
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "config")
    private static @NotNull BotConfig botConfig = new BotConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "language")
    private static @NotNull LanguageConfig languageConfig = new LanguageConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "caching")
    private static @NotNull CachingConfig cachingConfig = new CachingConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "donate")
    private static @NotNull DonateConfig donateConfig = new DonateConfig();
}