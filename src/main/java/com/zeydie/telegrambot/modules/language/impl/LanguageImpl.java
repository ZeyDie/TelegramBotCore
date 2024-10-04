package com.zeydie.telegrambot.modules.language.impl;

import com.google.common.collect.Lists;
import com.pengrad.telegrambot.model.User;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.events.language.LanguageRegisterEvent;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.api.utils.FileUtil;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.exceptions.language.LanguageNotRegisteredException;
import com.zeydie.telegrambot.exceptions.language.LanguageRegisteredException;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.*;

@Getter
public class LanguageImpl implements ILanguage {
    private final @NotNull List<LanguageData> registeredLanguages = Lists.newArrayList();

    @Override
    public void preInit() {
        FileUtil.createFolder(LANGUAGE_FOLDER_FILE);
    }

    @SneakyThrows
    @Override
    public void init() {
        @Nullable val files = LANGUAGE_FOLDER_FILE.listFiles();

        @NonNull val languageDataRegister = new LanguageData(
                "\uD83C\uDDEC\uD83C\uDDE7 English",
                "en",
                null
        );

        if (files != null && files.length > 0)
            Arrays.stream(files)
                    .forEach(
                            file -> {
                                try {
                                    this.register(SGsonFile.createPretty(file).fromJsonToObject(languageDataRegister));
                                } catch (final LanguageRegisteredException exception) {
                                    exception.printStackTrace(System.out);
                                }
                            }
                    );
        else
            this.register(
                    SGsonFile.createPretty(
                            LANGUAGE_FOLDER_FILE.toPath().resolve(
                                    FileUtil.createFileNameWithType(
                                            "en",
                                            LANGUAGE_TYPE
                                    )
                            )
                    ).fromJsonToObject(languageDataRegister)
            );
    }

    @Override
    public void postInit() {
        @NonNull val languageRegisterEvent = new LanguageRegisterEvent();

        TelegramBotCore.getInstance().getLanguageEventHandler().handle(languageRegisterEvent);

        languageRegisterEvent.getLanguageRegister()
                .getLanguageDataList()
                .forEach(languageData -> {
                            try {
                                if (!this.isRegistered(languageData))
                                    this.register(languageData);
                            } catch (final LanguageRegisteredException exception) {
                                exception.printStackTrace(System.out);
                            }
                        }
                );
    }

    @Override
    public boolean register(@NonNull final LanguageData languageData) throws LanguageRegisteredException {
        @NonNull val label = languageData.getLabel();
        @NonNull val uniqueId = languageData.getUniqueId();

        if (isRegistered(languageData))
            throw new LanguageRegisteredException(uniqueId, label);

        this.registeredLanguages.add(this.initLangFile(languageData));

        LoggerUtil.info("{} ({}) was registered!", label, uniqueId);

        return true;
    }

    @Override
    public boolean isRegistered(@NonNull final LanguageData languageData) {
        return this.isRegistered(languageData.getUniqueId());
    }

    @Override
    public boolean isRegistered(@NonNull final String uniqueId) {
        return this.registeredLanguages.stream()
                .anyMatch(languageData -> languageData.getUniqueId().equals(uniqueId));
    }

    @Override
    public @Nullable LanguageData getLanguageData(@NonNull final LanguageData languageData) {
        return this.getLanguageData(languageData.getUniqueId());
    }

    @Override
    public @Nullable LanguageData getLanguageData(@NonNull final String uniqueId) {
        return this.registeredLanguages.stream()
                .filter(languageData -> languageData.getUniqueId().equals(uniqueId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public @NotNull String localizeObject(
            @Nullable final Object object,
            @NonNull final String key
    ) throws LanguageNotRegisteredException {
        if (object == null) return this.localize(key);

        switch (object) {
            case User user -> {
                return this.localize(user, key);
            }
            case Long chatId -> {
                return this.localize(chatId, key);
            }
            default -> throw new IllegalStateException("Unexpected value: " + object);
        }
    }

    @Override
    public @NotNull String localize(
            @Nullable final UserData userData,
            @NonNull final String key
    ) throws LanguageNotRegisteredException {
        return userData == null ? this.localize(key) : this.localize(userData.getUser(), key);
    }

    @Override
    public @NotNull String localize(
            @Nullable final User user,
            @NonNull final String key
    ) throws LanguageNotRegisteredException {
        return user == null ? this.localize(key) : this.localize(user.id(), key);
    }

    @Override
    public @NotNull String localize(
            final long id,
            @NonNull final String key
    ) throws LanguageNotRegisteredException {
        @Nullable val userData = TelegramBotCore.getInstance().getUserCache().getUserData(id);

        @NonNull var language = ConfigStore.getLanguageConfig().getDefaultLanguageId();

        if (userData != null) {
            @Nullable val userLanguage = userData.getLanguageCode();

            if (userLanguage != null && this.isRegistered(userLanguage))
                language = userLanguage;
        }

        @Nullable val languageData = this.getLanguageData(language);

        if (languageData != null)
            return languageData.localize(key);
        else throw new LanguageNotRegisteredException(language, language);
    }

    @Override
    public @NotNull String localize(@NonNull final String key) throws LanguageNotRegisteredException {
        @NonNull val language = ConfigStore.getLanguageConfig().getDefaultLanguageId();
        @Nullable val languageData = this.getLanguageData(language);

        if (languageData != null)
            return languageData.localize(key);
        else throw new LanguageNotRegisteredException(language, language);
    }

    public @NotNull LanguageData initLangFile(@NonNull final LanguageData languageData) {
        return SGsonFile.createPretty(
                FileUtil.createFileWithNameAndType(LANGUAGE_FOLDER_PATH, languageData.getUniqueId(), LANGUAGE_TYPE)
        ).fromJsonToObject(languageData);
    }
}