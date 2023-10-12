package com.zeydie.telegrambot.api.utils;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@Log4j2
public final class ReflectionUtil {
    @SneakyThrows
    public static Object instance(@NotNull final Class<?> clazz) {
        return clazz.newInstance();
    }

    public static Class<?> getClassField(@NotNull final Field field) {
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