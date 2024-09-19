package com.zeydie.telegrambot.api.modules.permissions.data;

import com.zeydie.telegrambot.utils.RequestUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PermissionData {
    private final String permission;
    private String description;
    private String expireDate;

    private boolean hasExpire() {
        return this.expireDate != null;
    }

    public boolean isExpired() {
        if (!this.hasExpire()) return false;

        return System.currentTimeMillis() > RequestUtil.getMillisFromDate(this.expireDate);
    }
}