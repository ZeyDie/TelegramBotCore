package com.zeydie.telegrambot.core.impl.modules.permissions;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.api.modules.permissions.data.ListPermissionData;
import com.zeydie.telegrambot.api.modules.permissions.data.PermissionData;
import com.zeydie.telegrambot.core.utils.FileUtil;
import com.zeydie.telegrambot.core.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static com.zeydie.telegrambot.core.utils.ReferencePaths.*;

public class UserPermissionsImpl implements IPermissions {
    @Autowired
    private IUserCache userCache;

    private final @NotNull Cache<Long, ListPermissionData> usersPermissionsCache = CacheBuilder.newBuilder().build();

    @Override
    public void preInit() {
        FileUtil.createFolder(PERMISSIONS_FOLDER_FILE);
    }

    @Override
    public void init() {
        @Nullable val files = PERMISSIONS_FOLDER_FILE.listFiles();

        if (files != null)
            Arrays.stream(files)
                    .forEach(file -> {
                                try {
                                    LoggerUtil.debug("Restoring {}", file.getName());

                                    val userId = Long.parseLong(FileUtil.getFileName(file));
                                    @NonNull val permissionData = SGsonFile.createPretty(file).fromJsonToObject(new ListPermissionData());

                                    if (permissionData.getPermissions().isEmpty()) FileUtil.deleteFile(file);
                                    else {
                                        LoggerUtil.debug("User {} restored {}", userId, permissionData);
                                        this.usersPermissionsCache.put(userId, permissionData);
                                    }
                                } catch (final Exception exception) {
                                    LoggerUtil.error(exception);
                                }
                            }
                    );
    }

    @Override
    public void postInit() {
        this.usersPermissionsCache.cleanUp();
    }

    @Override
    public void save() {
        this.postInit();

        this.usersPermissionsCache.asMap()
                .forEach(
                        (id, userData) -> {
                            if (userData.getPermissions().isEmpty()) return;

                            LoggerUtil.debug("Saving user data permissions for {}", id);
                            SGsonFile.createPretty(FileUtil.createFileWithNameAndType(PERMISSIONS_FOLDER_PATH, id, PERMISSION_TYPE)).writeJsonFile(userData);
                        }
                );
    }

    @Override
    public void addPermission(
            @NonNull final UserData userData,
            @NonNull final String permission
    ) {
        this.addPermission(userData, new PermissionData(permission));
    }

    @Override
    public void addPermission(
            final long chatId,
            @NonNull final String permission
    ) {
        this.addPermission(chatId, new PermissionData(permission));
    }

    @Override
    public void addPermission(
            @NonNull final UserData userData,
            @NonNull final PermissionData permission
    ) {
        this.addPermission(userData.getUser().id(), permission);
    }

    @Override
    public void addPermission(
            final long chatId,
            @NonNull final PermissionData permission
    ) {
        if (this.hasPermission(chatId, permission)) return;

        @Nullable var permissionData = this.getPermissionData(chatId);

        if (permissionData == null) {
            permissionData = new ListPermissionData();

            this.usersPermissionsCache.put(chatId, permissionData);
        }

        permissionData.getPermissions().add(permission);
    }

    @Override
    public boolean hasPermission(
            @NonNull final UserData userData,
            @NonNull final String permission
    ) {
        return this.hasPermission(userData, new PermissionData(permission));
    }

    @Override
    public boolean hasPermission(
            final long chatId,
            @NonNull final String permission
    ) {
        return this.hasPermission(chatId, new PermissionData(permission));
    }

    @Override
    public boolean hasPermission(
            @NonNull final UserData userData,
            @NonNull final PermissionData permission
    ) {
        return this.hasPermission(userData.getUser().id(), permission);
    }

    @Override
    public boolean hasPermission(
            final long chatId,
            @NonNull final PermissionData permission
    ) {
        val userData = this.userCache.getUserData(chatId);

        if (userData != null && userData.isAdmin()) return true;

        @Nullable val permissionData = this.getPermissionData(chatId);

        if (permissionData == null)
            return false;

        return permissionData.contains("*") || permissionData.contains(permission);
    }

    @Override
    public @Nullable ListPermissionData getPermissionData(@NonNull final UserData userData) {
        return this.getPermissionData(userData.getUser().id());
    }

    @Override
    public @Nullable ListPermissionData getPermissionData(final long chatId) {
        return this.usersPermissionsCache.getIfPresent(chatId);
    }

    @Override
    public void removePermission(
            @NonNull final UserData userData,
            @NonNull final String permission
    ) {
        this.removePermission(userData, new PermissionData(permission));
    }

    @Override
    public void removePermission(
            final long chatId,
            @NonNull final String permission
    ) {
        this.removePermission(chatId, new PermissionData(permission));
    }

    @Override
    public void removePermission(
            @NonNull final UserData userData,
            @NonNull final PermissionData permission
    ) {
        this.removePermission(userData.getUser().id(), permission);
    }

    @Override
    public void removePermission(
            final long chatId,
            @NonNull final PermissionData permission
    ) {
        @Nullable val permissionData = this.getPermissionData(chatId);

        if (permissionData != null)
            permissionData.remove(permission);
    }

    @Override
    public void removePermissions(@NonNull final UserData userData) {
        this.removePermissions(userData.getUser().id());
    }

    @Override
    public void removePermissions(final long chatId) {
        this.usersPermissionsCache.asMap().remove(chatId);
    }
}