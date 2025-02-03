package com.zeydie.telegrambot.api.events.language;

import com.google.common.collect.Lists;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import com.zeydie.telegrambot.api.modules.language.data.LanguageData;
import com.zeydie.telegrambot.exceptions.language.LanguageRegisteredException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LanguageRegisterEvent extends AbstractEvent {
    private final @NotNull LanguageRegister languageRegister;

    public LanguageRegisterEvent() {
        this(new LanguageRegister());
    }

    public LanguageRegisterEvent(@NonNull final LanguageRegister languageRegister) {
        this.languageRegister = languageRegister;
    }

    @Getter
    public static class LanguageRegister {
        private final @NotNull List<LanguageData> languageDataList = Lists.newArrayList();

        public void register(@NonNull final LanguageData languageData) throws LanguageRegisteredException {
            if (this.languageDataList.contains(languageData))
                throw new LanguageRegisteredException(languageData);

            this.languageDataList.add(languageData);
        }
    }
}