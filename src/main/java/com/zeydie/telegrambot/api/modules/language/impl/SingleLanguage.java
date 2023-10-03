package com.zeydie.telegrambot.api.modules.language.impl;

import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.api.utils.FileUtil;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Log
public class SingleLanguage implements ILanguage {
    public final Path LANGUAGE_FOLDER = Paths.get("language");
    private LanguageData defaultLanguage;

    @Override
    public void register(@NotNull final LanguageData languageData) throws Exception {
        final String label = languageData.getLabel();
        final String uniqueId = languageData.getUniqueId();

        if (isRegistered(languageData))
            throw new LanguageRegisteredException(uniqueId, label);

        this.defaultLanguage = this.initLangFile(languageData);

        this.log.info(
                String.format(
                        "%s (%s) was registerd!",
                        label,
                        uniqueId
                )
        );
    }

    @Override
    public boolean isRegistered(@NotNull final LanguageData languageData) {
        return isRegistered(languageData.getUniqueId());
    }

    @Override
    public boolean isRegistered(@NotNull final String uniqueId) {
        return this.defaultLanguage == null ? false : this.defaultLanguage.getUniqueId().equals(uniqueId);
    }

    @Override
    public @Nullable LanguageData getLanguageData(@Nullable final LanguageData languageData) {
        return this.defaultLanguage;
    }

    @Override
    public @Nullable LanguageData getLanguageData(@Nullable final String uniqueId) {
        return this.defaultLanguage;
    }

    public @NotNull LanguageData initLangFile(@NotNull final LanguageData languageData) {
        languageData.setMessages(Map.of("welcome", "Hello!"));

        return new SGsonFile(
                LANGUAGE_FOLDER.resolve(
                        FileUtil.createFileWithType(
                                languageData.getUniqueId(),
                                "json"
                        )
                )
        )
                .fromJsonToObject(languageData);
    }
}
