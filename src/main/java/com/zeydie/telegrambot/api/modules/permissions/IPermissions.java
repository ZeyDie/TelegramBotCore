package com.zeydie.telegrambot.api.modules.permissions;

import com.zeydie.telegrambot.api.modules.interfaces.IData;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.permissions.data.PermissionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPermissions extends IData {
    void addPermission(
            @NotNull final UserData userData,
            @NotNull final String permission
    );

    void addPermission(
            final long chatId,
            @NotNull final String permission
    );

    boolean hasPermission(
            @NotNull final UserData userData,
            @NotNull final String permission
    );

    boolean hasPermission(
            final long chatId,
            @NotNull final String permission
    );

    @Nullable
    PermissionData getPermissionData(@NotNull final UserData userData);

    @Nullable
    PermissionData getPermissionData(final long chatId);

    void removePermission(
            @NotNull final UserData userData,
            @NotNull final String permissions
    );

    void removePermission(
            final long chatId,
            @NotNull final String permission
    );

    void removePermissions(@NotNull final UserData userData);

    void removePermissions(final long chatId);
}
