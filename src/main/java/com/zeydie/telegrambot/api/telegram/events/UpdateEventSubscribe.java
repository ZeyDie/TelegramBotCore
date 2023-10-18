package com.zeydie.telegrambot.api.telegram.events;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateEventSubscribe {
    @Nullable String comment() default "";
}