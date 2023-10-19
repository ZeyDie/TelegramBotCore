package com.zeydie.telegrambot.api.modules.permissions.data;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record PermissionData(@Nullable List<String> permissions) {
    public PermissionData {
        if (permissions == null)
            permissions = new ArrayList<>();
    }
}
