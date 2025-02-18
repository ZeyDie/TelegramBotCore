package com.zeydie.telegrambot.core.impl.chat.commands;

import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.core.configs.ConfigStore;
import com.zeydie.telegrambot.core.impl.chat.buttons.LanguageButton;
import com.zeydie.telegrambot.core.impl.modules.keyboard.MessageKeyboardImpl;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EventSubscribesRegister
public final class StartCommand {
    @Autowired
    private IUserCache userCache;
    @Autowired
    private ILanguage language;

    @CommandEventSubscribe(commands = "/start")
    public void start(@NonNull final CommandEvent event) {
        if (!ConfigStore.getLanguageConfig().isEnableLanguageSelector()) return;

        @NonNull val sender = event.getSender();

        if (!this.userCache.contains(sender)) {
            this.userCache.put(new UserData(sender));

            @NonNull val registeredLanguages = this.language.getRegisteredLanguages();

            @NonNull val languageCode = sender.languageCode();
            @Nullable val languageData = this.language.getLanguageData(this.language.isRegistered(languageCode) ? languageCode : "en");

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