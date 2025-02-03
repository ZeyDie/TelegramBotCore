package com.zeydie.telegrambot.api.modules.cache.users.data;

import com.pengrad.telegrambot.model.User;
import lombok.Data;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.Nullable;

@Data
public class UserData {
    private final User user;
    @NonFinal
    private boolean admin;
    @NonFinal
    private @Nullable String languageCode;
}