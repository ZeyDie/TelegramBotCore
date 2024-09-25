package com.zeydie.telegrambot.chat.commands;

import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.configs.ConfigStore;
import lombok.NonNull;
import lombok.val;

import java.util.concurrent.CompletableFuture;

@EventSubscribesRegister
public final class ReloadCommand {
    @CommandEventSubscribe(commands = "/reload", permissions = "telegrambotcore.reload")
    public void reload(@NonNull final CommandEvent event) {
        if (!ConfigStore.getBotConfig().isEnableReloadCommand()) return;

        @NonNull val instance = TelegramBotCore.getInstance();

        instance.sendMessage(event.getSender(), "messages.reloaded");

        CompletableFuture.runAsync(instance::stop).thenRun(instance::launch);
    }
}