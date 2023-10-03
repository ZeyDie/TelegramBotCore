package com.zeydie.telegrambot.api.modules.language.data;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.NonFinal;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public final class LanguageData {
    @NonFinal
    private String label;
    @NonFinal
    private String uniqueId;
    @NonFinal
    @Builder.Default
    private Map<String, String> messages = new HashMap<>();
}
