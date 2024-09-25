package com.zeydie.telegrambot.api.telegram.events.subscribes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandEventSubscribe {
    @NotNull String[] commands();

    @NotNull String[] permissions() default {};

    @Nullable String comment() default "";
}