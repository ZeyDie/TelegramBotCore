package com.zeydie.telegrambot.core.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public final class ReflectionUtil {
    @SneakyThrows
    public static @NotNull Object instance(@NonNull final Class<?> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }

    public static @NotNull Class<?> getClassField(@NonNull final Field field) {
        return field.getType();
    }

    @SneakyThrows
    public static @Nullable Object getValueField(
            @NonNull final Field field,
            @NonNull final Object classInstance
    ) {
        return field.trySetAccessible() ? field.get(classInstance) : null;
    }

    @SneakyThrows
    public static void setValueField(
            @NonNull final Field field,
            @NonNull final Object classInstance,
            @Nullable final Object value
    ) {
        field.setAccessible(true);
        field.set(classInstance, value);
    }
}