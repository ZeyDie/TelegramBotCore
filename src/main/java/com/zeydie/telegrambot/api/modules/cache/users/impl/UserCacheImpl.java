package com.zeydie.telegrambot.api.modules.cache.users.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.pengrad.telegrambot.model.User;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.configs.AbstractFileConfig;
import com.zeydie.telegrambot.api.utils.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Log
public final class UserCacheImpl implements IUserCache {
    @NotNull
    private final Cache<Long, UserData> userDataCache = CacheBuilder.newBuilder()
            .expireAfterWrite(4, TimeUnit.HOURS)
            .removalListener((RemovalListener<Long, UserData>) notification -> {
                final long userId = notification.getKey();
                final UserData userData = notification.getValue();

                log.warning(String.format("Cleanup %d %s", userId, userData));
            })
            .build();

    @SneakyThrows
    @Override
    public void init() {
        AbstractFileConfig.CACHE_USERS_FOLDER.toFile().mkdirs();

        for (final File file : AbstractFileConfig.CACHE_USERS_FOLDER.toFile().listFiles()) {
            final long userId = Long.valueOf(FileUtil.getFileName(file));
            final UserData userData = new SGsonFile(file).fromJsonToObject(new UserData());

            log.info(String.format("User %d restored %s", userId, userData));

            this.userDataCache.put(userId, userData);
        }
    }

    @Override
    public void shutdown() {
        AbstractFileConfig.CACHE_USERS_FOLDER.toFile().mkdirs();

        this.userDataCache.asMap().forEach((id, userData) -> {
            new SGsonFile(
                    AbstractFileConfig.CACHE_USERS_FOLDER.resolve(FileUtil.createFileWithType(id, ".json"))
            ).writeJsonFile(userData);

            log.info(String.format("Saving user data cache for %d", id));
        });
    }

    @Override
    public boolean contains(@NotNull final User user) {
        return this.contains(user.id());
    }

    @Override
    public boolean contains(final long userId) {
        return this.userDataCache.asMap().containsKey(userId);
    }

    @Override
    public void put(@NotNull final User user) {
        if (!this.contains(user))
            this.userDataCache.put(user.id(), new UserData(user));
    }

    @Nullable
    @Override
    public UserData getUserData(@NotNull final User user) {
        return this.getUserData(user.id());
    }

    @Nullable
    @Override
    public UserData getUserData(final long userId) {
        return this.userDataCache.getIfPresent(userId);
    }
}
