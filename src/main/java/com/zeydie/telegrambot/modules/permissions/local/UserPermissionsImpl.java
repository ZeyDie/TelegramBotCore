package com.zeydie.telegrambot.modules.permissions.local;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.api.modules.permissions.data.PermissionData;
import com.zeydie.telegrambot.utils.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.zeydie.telegrambot.utils.ReferencePaths.PERMISSIONS_FOLDER_PATH;
import static com.zeydie.telegrambot.utils.ReferencePaths.PERMISSION_TYPE;

@Log4j2
public class UserPermissionsImpl implements IPermissions {
    private final @NotNull Cache<Long, PermissionData> usersPermissionsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(4, TimeUnit.HOURS)
            .removalListener((RemovalListener<Long, PermissionData>) notification -> {
                        final long userId = notification.getKey();
                        @NotNull final PermissionData permissionData = notification.getValue();

                        log.debug("Cleanup {} {}", userId, permissionData);

                        if (permissionData.permissions().isEmpty()) {
                            @NotNull final File file = FileUtil.createFileWithNameAndType(PERMISSIONS_FOLDER_PATH, userId, PERMISSION_TYPE);

                            if (file.exists())
                                file.delete();
                        }
                    }
            ).build();

    @Override
    public void load() {
        PERMISSIONS_FOLDER_PATH.toFile().mkdirs();

        Arrays.stream(Objects.requireNonNull(PERMISSIONS_FOLDER_PATH.toFile().listFiles()))
                .forEach(file -> {
                            try {
                                log.info("Restoring {}", file.getName());

                                final long userId = Long.parseLong(FileUtil.getFileName(file));
                                @NotNull final PermissionData permissionData = new SGsonFile(file).fromJsonToObject(new PermissionData(null));

                                this.usersPermissionsCache.put(userId, permissionData);

                                log.info("User {} restored {}", userId, permissionData);
                            } catch (final Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                );
    }

    @Override
    public void save() {
        PERMISSIONS_FOLDER_PATH.toFile().mkdirs();

        this.usersPermissionsCache.asMap().forEach((id, userData) -> {
                    log.info("Saving user data permissions for {}", id);
                    new SGsonFile(PERMISSIONS_FOLDER_PATH.resolve(FileUtil.createFileNameWithType(id, PERMISSION_TYPE))).writeJsonFile(userData);
                }
        );
    }

    @Override
    public void addPermission(
            @NotNull final UserData userData,
            @NotNull final String permission
    ) {
        this.addPermission(userData.getUser().id(), permission);
    }

    @Override
    public void addPermission(
            final long chatId,
            @NotNull final String permission
    ) {
        @Nullable PermissionData permissionData = this.getPermissionData(chatId);

        if (permissionData == null) {
            permissionData = new PermissionData(null);

            this.usersPermissionsCache.put(chatId, permissionData);
        }

        permissionData.permissions().add(permission);
    }

    @Override
    public boolean hasPermission(
            @NotNull final UserData userData,
            @NotNull final String permission
    ) {
        return this.hasPermission(userData.getUser().id(), permission);
    }

    @Override
    public boolean hasPermission(
            final long chatId,
            @NotNull final String permission
    ) {
        @Nullable final PermissionData permissionData = this.getPermissionData(chatId);

        if (permissionData == null)
            return false;

        return permissionData.permissions().contains(permission);
    }

    @Override
    public @Nullable PermissionData getPermissionData(@NotNull final UserData userData) {
        return this.getPermissionData(userData.getUser().id());
    }

    @Override
    public @Nullable PermissionData getPermissionData(final long chatId) {
        return this.usersPermissionsCache.getIfPresent(chatId);
    }

    @Override
    public void removePermission(
            @NotNull final UserData userData,
            @NotNull final String permission
    ) {
        this.removePermission(userData.getUser().id(), permission);
    }

    @Override
    public void removePermission(
            final long chatId,
            @NotNull final String permission
    ) {
        @Nullable final PermissionData permissionData = this.getPermissionData(chatId);

        if (permissionData != null)
            permissionData.permissions().remove(permission);
    }

    @Override
    public void removePermissions(@NotNull final UserData userData) {
        this.removePermissions(userData.getUser().id());
    }

    @Override
    public void removePermissions(final long chatId) {
        @NotNull final Map<Long, PermissionData> usersPermissionsMap = this.usersPermissionsCache.asMap();

        if (usersPermissionsMap.containsKey(chatId))
            usersPermissionsMap.remove(chatId);
    }
}
