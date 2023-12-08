package com.zeydie.telegrambot.api.modules.permissions;

import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.api.modules.interfaces.IData;
import com.zeydie.telegrambot.api.modules.permissions.data.PermissionData;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface IPermissions extends IData {
    void addPermission(
            @NonNull final UserData userData,
            @NonNull final String permission
    );

    void addPermission(
            final long chatId,
            @NonNull final String permission
    );

    boolean hasPermission(
            @NonNull final UserData userData,
            @NonNull final String permission
    );

    boolean hasPermission(
            final long chatId,
            @NonNull final String permission
    );

    @Nullable
    PermissionData getPermissionData(@NonNull final UserData userData);

    @Nullable
    PermissionData getPermissionData(final long chatId);

    void removePermission(
            @NonNull final UserData userData,
            @NonNull final String permissions
    );

    void removePermission(
            final long chatId,
            @NonNull final String permission
    );

    void removePermissions(@NonNull final UserData userData);

    void removePermissions(final long chatId);
}
