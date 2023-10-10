package com.zeydie.telegrambot.api.modules.cache.users.data;

import com.pengrad.telegrambot.model.User;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class UserData {
    private final User user;
    private @Nullable String languageUniqueId;
}