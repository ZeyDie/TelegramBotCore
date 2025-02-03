package com.zeydie.telegrambot.api.modules.cache.users;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.interfaces.IData;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface IUserCache extends IData {
    boolean contains(@NonNull final UserData userData);

    boolean contains(@NonNull final User user);

    boolean contains(final long userId);

    void put(@NonNull final UserData userData);

    void put(@NonNull final User user);

    @Nullable
    UserData getUserData(@NonNull final User user);

    @Nullable
    UserData getUserData(final long userId);
}