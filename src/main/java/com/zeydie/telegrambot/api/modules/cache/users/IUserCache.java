package com.zeydie.telegrambot.api.modules.cache.users;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.modules.cache.IDataCache;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IUserCache extends IDataCache {
    boolean contains(@NotNull final User user);

    boolean contains(final long userId);

    void put(@NotNull final User user);

    @Nullable
    UserData getUserData(@NotNull final User user);

    @Nullable
    UserData getUserData(final long userId);
}
