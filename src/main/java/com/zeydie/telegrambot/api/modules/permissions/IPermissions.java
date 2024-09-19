package com.zeydie.telegrambot.api.modules.permissions;

import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.interfaces.IData;
import com.zeydie.telegrambot.api.modules.permissions.data.PermissionData;
import com.zeydie.telegrambot.api.modules.permissions.data.PermissionsData;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface IPermissions extends IData {
    void addPermission(
            @NonNull final UserData userData,
            @NonNull final PermissionData permission
    );

    void addPermission(
            final long chatId,
            @NonNull final PermissionData permission
    );

    boolean hasPermission(
            @NonNull final UserData userData,
            @NonNull final PermissionData permission
    );

    boolean hasPermission(
            final long chatId,
            @NonNull final PermissionData permission
    );

    @Nullable
    PermissionsData getPermissionData(@NonNull final UserData userData);

    @Nullable
    PermissionsData getPermissionData(final long chatId);

    void removePermission(
            @NonNull final UserData userData,
            @NonNull final PermissionData permission
    );

    void removePermission(
            final long chatId,
            @NonNull final PermissionData permission
    );

    void removePermissions(@NonNull final UserData userData);

    void removePermissions(final long chatId);
}