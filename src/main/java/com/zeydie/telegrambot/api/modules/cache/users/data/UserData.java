package com.zeydie.telegrambot.api.modules.cache.users.data;

import com.pengrad.telegrambot.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private User user;
}
