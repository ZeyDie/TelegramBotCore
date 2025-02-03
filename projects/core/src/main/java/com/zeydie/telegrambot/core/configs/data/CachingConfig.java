package com.zeydie.telegrambot.core.configs.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.NonFinal;

@Data
@EqualsAndHashCode(callSuper = false)
public final class CachingConfig {
    @NonFinal
    private boolean caching = true;
}