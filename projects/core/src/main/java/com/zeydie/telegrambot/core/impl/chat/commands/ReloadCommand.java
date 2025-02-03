package com.zeydie.telegrambot.core.impl.chat.commands;

import com.zeydie.telegrambot.core.TelegramBotCore;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.core.configs.ConfigStore;
import com.zeydie.telegrambot.core.utils.SendMessageUtil;
import lombok.NonNull;
import lombok.val;

import java.util.concurrent.CompletableFuture;

@EventSubscribesRegister
public final class ReloadCommand {
    @CommandEventSubscribe(commands = "/reload", permissions = "telegrambotcore.reload")
    public void reload(@NonNull final CommandEvent event) {
        if (!ConfigStore.getBotConfig().isEnableReloadCommand()) return;

        @NonNull val instance = TelegramBotCore.getInstance();

        SendMessageUtil.sendMessage(event.getSender(), "messages.reloaded");

        CompletableFuture.runAsync(instance::stop).thenRun(instance::launch);
    }
}