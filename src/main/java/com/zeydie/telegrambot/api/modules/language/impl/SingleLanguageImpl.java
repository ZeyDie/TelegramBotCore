package com.zeydie.telegrambot.api.modules.language.impl;

import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.exceptions.LanguageRegisteredException;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.api.utils.FileUtil;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.LANGUAGE_FOLDER;

@Log
public class SingleLanguageImpl implements ILanguage {
    private LanguageData defaultLanguage;

    @Override
    public void register(@NotNull final LanguageData languageData) throws Exception {
        final String label = languageData.label();
        final String uniqueId = languageData.uniqueId();

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

    @NotNull
    @Override
    public List<LanguageData> getRegisteredLanguages() {
        return Collections.singletonList(this.defaultLanguage);
    }

    @Override
    public boolean isRegistered(@NotNull final LanguageData languageData) {
        return isRegistered(languageData.uniqueId());
    }

    @Override
    public boolean isRegistered(@NotNull final String uniqueId) {
        return this.defaultLanguage == null ? false : this.defaultLanguage.uniqueId().equals(uniqueId);
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
        languageData.localization().putAll(Map.of("welcome", "Hello!"));

        return new SGsonFile(
                LANGUAGE_FOLDER.resolve(
                        FileUtil.createFileWithType(
                                languageData.uniqueId(),
                                "json"
                        )
                )
        )
                .fromJsonToObject(languageData);
    }
}