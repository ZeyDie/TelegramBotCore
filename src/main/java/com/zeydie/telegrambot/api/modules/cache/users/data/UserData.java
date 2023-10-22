package com.zeydie.telegrambot.api.modules.cache.users.data;

import com.pengrad.telegrambot.model.User;
import lombok.Data;

@Data
public class UserData {
    private final User user;
    private String languageUniqueId;
}