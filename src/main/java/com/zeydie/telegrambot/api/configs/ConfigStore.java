package com.zeydie.telegrambot.api.configs;

import com.zeydie.telegrambot.api.configs.data.BotChatFileConfig;
import com.zeydie.telegrambot.api.configs.data.BotFileConfig;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import lombok.Getter;
import lombok.experimental.NonFinal;

@ConfigSubscribesRegister
public class ConfigStore {
    @Getter
    public static final ConfigStore configStore = new ConfigStore();

    @Getter
    @NonFinal
    @ConfigSubscribe(name = "bot")
    public BotFileConfig botFileConfig = new BotFileConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "bot_chat")
    public BotChatFileConfig botChatFileConfig = new BotChatFileConfig();
}