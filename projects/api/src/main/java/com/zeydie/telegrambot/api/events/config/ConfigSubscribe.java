package com.zeydie.telegrambot.api.events.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigSubscribe {
    @NotNull String name();

    boolean file() default true;

    @NotNull Category category() default Category.CONFIGS;

    @Nullable String path() default "";

    @Nullable String comment() default "";
}