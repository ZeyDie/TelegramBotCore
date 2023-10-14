package com.zeydie.telegrambot.api.telegram.events.message;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageEventSubscribe {
    @Nullable String comment() default "";
}