package com.zeydie.telegrambot.modules.cache.users.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.pengrad.telegrambot.model.User;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.utils.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.zeydie.telegrambot.utils.ReferencePaths.CACHE_USERS_FOLDER_PATH;
import static com.zeydie.telegrambot.utils.ReferencePaths.DATA_TYPE;

@Log4j2
public class UserCacheImpl implements IUserCache {
    private final @NotNull Cache<Long, UserData> userDataCache = CacheBuilder.newBuilder()
            .expireAfterWrite(4, TimeUnit.HOURS)
            .removalListener((RemovalListener<Long, UserData>) notification -> {
                        final long userId = notification.getKey();
                        @NotNull final UserData userData = notification.getValue();

                        log.debug("Cleanup {} {}", userId, userData);
                    }
            ).build();

    @SneakyThrows
    @Override
    public void load() {
        CACHE_USERS_FOLDER_PATH.toFile().mkdirs();

        Arrays.stream(Objects.requireNonNull(CACHE_USERS_FOLDER_PATH.toFile().listFiles()))
                .forEach(file -> {
                            try {
                                log.info("Restoring {}", file.getName());

                                final long userId = Long.parseLong(FileUtil.getFileName(file));
                                @NotNull final UserData userData = new SGsonFile(file).fromJsonToObject(new UserData(null));

                                this.userDataCache.put(userId, userData);

                                log.info("User {} restored {}", userId, userData);
                            } catch (final Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                );
    }

    @Override
    public void save() {
        CACHE_USERS_FOLDER_PATH.toFile().mkdirs();

        this.userDataCache.asMap().forEach((id, userData) -> {
                    log.info("Saving user data cache for {}", id);
                    new SGsonFile(CACHE_USERS_FOLDER_PATH.resolve(FileUtil.createFileWithType(id, DATA_TYPE))).writeJsonFile(userData);
                }
        );
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

    @Override
    public @NotNull UserData getUserData(@NotNull final User user) {
        @Nullable final UserData userData = this.getUserData(user.id());

        return userData == null ? new UserData(user) : userData;
    }

    @Override
    public @Nullable UserData getUserData(final long userId) {
        return this.userDataCache.getIfPresent(userId);
    }
}