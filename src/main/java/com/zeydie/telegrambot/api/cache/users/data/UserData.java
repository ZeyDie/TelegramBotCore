package com.zeydie.telegrambot.api.cache.users.data;

import com.pengrad.telegrambot.model.User;
import lombok.Data;

@Data
public class UserData {
    private User user;

    public UserData() {
    }

    public UserData(final User user) {
        this.user = user;
    }
}
