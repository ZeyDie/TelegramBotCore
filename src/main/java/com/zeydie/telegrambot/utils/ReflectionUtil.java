package com.zeydie.telegrambot.utils;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public final class ReflectionUtil {
    @SneakyThrows
    public static @NotNull Object instance(@NotNull final Class<?> clazz) {
        return clazz.newInstance();
    }

    public static @NotNull Class<?> getClassField(@NotNull final Field field) {
        return field.getType();
    }

    @SneakyThrows
    public static @Nullable Object getValueField(
            @NotNull final Field field,
            @NotNull final Object classInstance
    ) {
        return field.trySetAccessible() ? field.get(classInstance) : null;
    }

    @SneakyThrows
    public static void setValueField(
            @NotNull final Field field,
            @NotNull final Object classInstance,
            @Nullable final Object value
    ) {
        final boolean accessible = field.isAccessible();

        field.setAccessible(true);
        field.set(classInstance, value);
        field.setAccessible(accessible);
    }
}