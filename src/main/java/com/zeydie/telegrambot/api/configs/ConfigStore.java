package com.zeydie.telegrambot.api.configs;

import com.zeydie.telegrambot.api.configs.data.BotChatFileConfig;
import com.zeydie.telegrambot.api.configs.data.BotFileConfig;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@ConfigSubscribesRegister
public class ConfigStore {
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "bot")
    public static @NotNull BotFileConfig botFileConfig = new BotFileConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "bot_chat")
    public static @NotNull BotChatFileConfig botChatFileConfig = new BotChatFileConfig();
}