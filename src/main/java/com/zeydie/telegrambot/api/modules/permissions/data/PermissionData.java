package com.zeydie.telegrambot.api.modules.permissions.data;

import com.zeydie.telegrambot.api.utils.RequestUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@RequiredArgsConstructor
public class PermissionData {
    private final @NotNull String permission;
    private @Nullable String description;
    private @Nullable String expireDate;

    private boolean hasExpire() {
        return this.expireDate != null;
    }

    public boolean isExpired() {
        if (!this.hasExpire()) return false;

        return System.currentTimeMillis() > RequestUtil.getMillisFromDate(this.expireDate);
    }
}