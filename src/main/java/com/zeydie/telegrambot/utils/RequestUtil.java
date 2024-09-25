package com.zeydie.telegrambot.utils;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static @NotNull String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static @NotNull String getDateWithFormat(final long millis) {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date(millis));
    }

    @SneakyThrows
    public static long getMillisFromDate(@NonNull final String date) {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(date).getTime();
    }
}