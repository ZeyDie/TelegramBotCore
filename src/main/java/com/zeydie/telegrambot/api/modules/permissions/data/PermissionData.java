package com.zeydie.telegrambot.api.modules.permissions.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record PermissionData(@NotNull List<String> permissions) {
    public PermissionData {
        if (permissions == null)
            permissions = new ArrayList<>();
    }
}
