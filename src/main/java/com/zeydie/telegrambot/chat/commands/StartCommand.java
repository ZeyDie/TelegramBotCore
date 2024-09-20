package com.zeydie.telegrambot.chat.commands;

import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.api.telegram.events.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.chat.buttons.LanguageButton;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.modules.keyboard.impl.MessageKeyboardImpl;
import com.zeydie.telegrambot.telegram.events.CommandEvent;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@EventSubscribesRegister
public final class StartCommand {
    @CommandEventSubscribe(commands = "/start")
    public void start(@NonNull final CommandEvent event) {
        if (!ConfigStore.getLanguageConfig().isEnableLanguageSelector()) return;

        @NonNull val sender = event.getSender();

        @NonNull val instance = TelegramBotCore.getInstance();
        @NonNull val userCache = instance.getUserCache();

        if (!userCache.contains(sender)) {
            userCache.put(new UserData(sender));

            @NonNull val language = instance.getLanguage();
            @NonNull val registeredLanguages = language.getRegisteredLanguages();

            @NonNull val languageCode = sender.languageCode();
            @NonNull val languageData = language.getLanguageData(language.isRegistered(languageCode) ? languageCode : "en");

            @NonNull val messageKeyboard = new MessageKeyboardImpl();

            for (int i = 0; i < registeredLanguages.size(); i++) {
                @NonNull val registeredLanguage = registeredLanguages.get(i);

                messageKeyboard.addButton(
                        LanguageButton.create(registeredLanguage.label())
                                .callbackData(
                                        "language.select." +
                                                registeredLanguage.uniqueId() +
                                                "." +
                                                sender.id()
                                )
                );

                if (i % 5 == 0)
                    messageKeyboard.completeRow();
            }

            messageKeyboard.sendKeyboard(sender, languageData.localize("messages.select_language"));
        }
    }
}