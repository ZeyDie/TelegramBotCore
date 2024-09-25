package com.zeydie.telegrambot.api.modules.permissions.data;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record PermissionsData(@Nullable List<PermissionData> permissions) {
    public PermissionsData {
        if (permissions == null)
            permissions = new ArrayList<>();
    }

    public boolean contains(@NonNull final PermissionData permission) {
        return this.contains(permission.getPermission());
    }

    public boolean remove(@NonNull final PermissionData permission) {
        return this.remove(permission.getPermission());
    }

    public boolean contains(@NonNull final String permission) {
        return this.permissions.stream().anyMatch(permissionData -> permissionData.getPermission().equals(permission));
    }

    public boolean remove(@NonNull final String permission) {
        return this.permissions.removeIf(permissionData -> permissionData.getPermission().equals(permission));
    }
}