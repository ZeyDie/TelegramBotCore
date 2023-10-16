package com.zeydie.telegrambot.utils;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class RequestUtil {
    public static final @NotNull String PARAMETER_CHAT_ID = "chat_id";
    public static final @NotNull String PARAMETER_TEXT = "text";

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Object getChatId(@NotNull final BaseRequest<T, R> baseRequest) {
        return baseRequest.getParameters().get(PARAMETER_CHAT_ID);
    }

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Object getText(@NotNull final BaseRequest<T, R> baseRequest) {
        return baseRequest.getParameters().get(PARAMETER_TEXT);
    }

    public static <T extends BaseRequest<T, R>, R extends BaseResponse> void setValue(
            @NotNull final BaseRequest<T, R> baseRequest,
            @NotNull final String key,
            @NotNull final Object value
    ) {
        @NotNull final Map<String, Object> parameters = baseRequest.getParameters();

        if (parameters.containsKey(key))
            parameters.replace(key, value);
    }
}
