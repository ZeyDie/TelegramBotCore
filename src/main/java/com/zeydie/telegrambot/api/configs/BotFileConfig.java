package com.zeydie.telegrambot.api.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.NonFinal;

@Data
@EqualsAndHashCode(callSuper = false)
public final class BotFileConfig extends AbstractFileConfig {
    @NonFinal
    private String name;
    @NonFinal
    private String token;

    public BotFileConfig() {
        super(BotFileConfig.class, "bot");
    }
}
