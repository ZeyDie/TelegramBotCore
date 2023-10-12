package com.zeydie.telegrambot.api.modules.language.impl;

import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.LANGUAGE_FOLDER_PATH;

@Log4j2
public class MultiLanguageImpl implements ILanguage {
    @Getter
    private final @NotNull List<LanguageData> registeredLanguages = new ArrayList<>();

    @Override
    public void load() {
        LANGUAGE_FOLDER_PATH.toFile().mkdirs();

        Arrays.stream(Objects.requireNonNull(LANGUAGE_FOLDER_PATH.toFile().listFiles()))
                .forEach(file -> {
                    try {
                        this.register(new SGsonFile(file).fromJsonToObject(
                                new LanguageData(
                                        null,
                                        null,
                                        null
                                )
                        ));
                    } catch (LanguageRegisteredException exception) {
                        exception.printStackTrace();
                    }
                });
    }

    @Override
    public boolean register(@NotNull final LanguageData languageData) throws LanguageRegisteredException {
        @NotNull final String label = languageData.label();
        @NotNull final String uniqueId = languageData.uniqueId();

        if (isRegistered(languageData))
            throw new LanguageRegisteredException(uniqueId, label);

        this.registeredLanguages.add(SingleLanguageImpl.initLangFile(languageData));

        log.info("{} ({}) was registered!", label, uniqueId);

        return true;
    }

    @Override
    public boolean isRegistered(@NotNull final LanguageData languageData) {
        return this.isRegistered(languageData.uniqueId());
    }

    @Override
    public boolean isRegistered(@NotNull final String uniqueId) {
        return this.registeredLanguages.stream()
                .anyMatch(languageData -> languageData.uniqueId().equals(uniqueId));
    }

    @Override
    public @Nullable LanguageData getLanguageData(@NotNull final LanguageData languageData) {
        return this.getLanguageData(languageData.uniqueId());
    }

    @Override
    public @Nullable LanguageData getLanguageData(@NotNull final String uniqueId) {
        return this.registeredLanguages.stream()
                .filter(languageData -> languageData.uniqueId().equals(uniqueId))
                .findFirst()
                .orElse(null);
    }
}