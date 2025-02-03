package com.zeydie.telegrambot.core.impl.chat.commands;

import com.zeydie.telegrambot.core.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.core.configs.ConfigStore;
import com.zeydie.telegrambot.core.impl.chat.buttons.LanguageButton;
import com.zeydie.telegrambot.core.impl.modules.keyboard.MessageKeyboardImpl;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

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
            @Nullable val languageData = language.getLanguageData(language.isRegistered(languageCode) ? languageCode : "en");

            if (languageData == null) return;

            @NonNull val messageKeyboard = new MessageKeyboardImpl(languageData.localize("messages.select_language"));

            for (int i = 0; i < registeredLanguages.size(); i++) {
                @NonNull val registeredLanguage = registeredLanguages.get(i);

                messageKeyboard.addButton(
                        LanguageButton.create(registeredLanguage.getLabel())
                                .callbackData(
                                        "language.select." +
                                                registeredLanguage.getUniqueId() +
                                                "." +
                                                sender.id()
                                )
                );

                if (i != 0 && i % 5 == 0)
                    messageKeyboard.completeRow();
            }

            messageKeyboard.sendKeyboard(sender);
        }
    }
}