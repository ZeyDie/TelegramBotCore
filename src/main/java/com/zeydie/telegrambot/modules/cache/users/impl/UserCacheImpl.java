package com.zeydie.telegrambot.modules.cache.users.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pengrad.telegrambot.model.User;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.utils.FileUtil;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.*;

public class UserCacheImpl implements IUserCache {
    private final @NotNull Cache<Long, UserData> userDataCache = CacheBuilder.newBuilder().build();

    @Override
    public void preInit() {
        FileUtil.createFolder(CACHE_USERS_FOLDER_FILE);
    }

    @Override
    public void init() {
        @Nullable val files = CACHE_USERS_FOLDER_FILE.listFiles();

        if (files != null)
            Arrays.stream(files)
                    .forEach(file -> {
                                try {
                                    val fileName = FileUtil.getFileName(file);

                                    LoggerUtil.info("Restoring {}", fileName);

                                    val userId = Long.parseLong(fileName);
                                    @NonNull val userData = SGsonFile.createPretty(file).fromJsonToObject(new UserData(null));

                                    LoggerUtil.info("User {} restored {}", userId, userData);
                                    this.userDataCache.put(userId, userData);
                                } catch (final Exception exception) {
                                    exception.printStackTrace(System.out);
                                }
                            }
                    );
    }

    @Override
    public void postInit() {
        this.userDataCache.cleanUp();
    }

    @Override
    public void save() {
        val startTime = System.currentTimeMillis();

        this.postInit();

        this.userDataCache.asMap()
                .forEach(
                        (id, userData) -> {
                            LoggerUtil.info("Saving user data cache for {}", id);
                            SGsonFile.createPretty(FileUtil.createFileWithNameAndType(CACHE_USERS_FOLDER_PATH, id, DATA_TYPE)).writeJsonFile(userData);
                        }
                );

        LoggerUtil.info("Saved data in {} sec.", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Override
    public boolean contains(@NonNull final UserData userData) {
        return this.contains(userData.getUser());
    }

    @Override
    public boolean contains(@NonNull final User user) {
        return this.contains(user.id());
    }

    @Override
    public boolean contains(final long userId) {
        return this.userDataCache.asMap().containsKey(userId);
    }

    @Override
    public void put(@NonNull final UserData userData) {
        if (this.contains(userData))
            return;

        this.userDataCache.put(userData.getUser().id(), userData);
    }

    @Override
    public void put(@NonNull final User user) {
        if (this.contains(user))
            return;

        this.userDataCache.put(user.id(), new UserData(user));
    }

    @Override
    public @Nullable UserData getUserData(@NonNull final User user) {
        if (!this.contains(user))
            this.put(user);

        return this.getUserData(user.id());
    }

    @Override
    public @Nullable UserData getUserData(final long userId) {
        return this.userDataCache.getIfPresent(userId);
    }
}