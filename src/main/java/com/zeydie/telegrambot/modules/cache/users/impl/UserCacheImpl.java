package com.zeydie.telegrambot.modules.cache.users.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pengrad.telegrambot.model.User;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.utils.FileUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.zeydie.telegrambot.utils.ReferencePaths.*;

@Log4j2
public class UserCacheImpl implements IUserCache {
    private final @NotNull Cache<Long, UserData> userDataCache = CacheBuilder.newBuilder().build();

    @Override
    public void preInit() {
        CACHE_USERS_FOLDER_FILE.mkdirs();
    }

    @SneakyThrows
    @Override
    public void init() {
        @Nullable val files = CACHE_USERS_FOLDER_FILE.listFiles();

        if (files != null)
            Arrays.stream(files)
                    .forEach(file -> {
                                try {
                                    log.info("Restoring {}", file.getName());

                                    val userId = Long.parseLong(FileUtil.getFileName(file));
                                    @NonNull val userData = new SGsonFile(file).fromJsonToObject(new UserData(null));

                                    log.info("User {} restored {}", userId, userData);
                                    this.userDataCache.put(userId, userData);
                                } catch (final Exception exception) {
                                    exception.printStackTrace();
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
        this.postInit();

        this.userDataCache.asMap().forEach((id, userData) -> {
                    log.info("Saving user data cache for {}", id);
                    new SGsonFile(FileUtil.createFileWithNameAndType(CACHE_USERS_FOLDER_PATH, id, DATA_TYPE)).writeJsonFile(userData);
                }
        );
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
        if (!this.contains(userData)) this.userDataCache.put(userData.getUser().id(), userData);
    }

    @Override
    public void put(@NonNull final User user) {
        if (!this.contains(user))
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