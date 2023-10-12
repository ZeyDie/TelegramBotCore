package com.zeydie.telegrambot.api.events.config;

import com.zeydie.telegrambot.api.utils.ReferencePaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigSubscribe {
    @NotNull String name();

    boolean file() default true;

    @NotNull ReferencePaths.Category category() default ReferencePaths.Category.CONFIGS;

    @Nullable String path() default "";

    @Nullable String comment() default "";
}
