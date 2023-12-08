package com.zeydie.telegrambot.utils;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RequestUtil {
    public static final @NotNull String PARAMETER_CHAT_ID = "chat_id";
    public static final @NotNull String PARAMETER_TEXT = "text";
    public static final @NotNull String PARAMETER_KEYBOARD = "reply_markup";

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Object getChatId(@NotNull final BaseRequest<T, R> baseRequest) {
        return baseRequest.getParameters().get(PARAMETER_CHAT_ID);
    }

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Object getText(@NotNull final BaseRequest<T, R> baseRequest) {
        return baseRequest.getParameters().get(PARAMETER_TEXT);
    }

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Object getKeyboard(@NotNull final BaseRequest<T, R> baseRequest) {
        return baseRequest.getParameters().get(PARAMETER_KEYBOARD);
    }

    public static <T extends BaseRequest<T, R>, R extends BaseResponse> void setValue(
            @NonNull final BaseRequest<T, R> baseRequest,
            @NonNull final String key,
            @NonNull final Object value
    ) {
        @NonNull val parameters = baseRequest.getParameters();

        if (parameters.containsKey(key))
            parameters.replace(key, value);
    }
}