package com.zeydie.telegrambot.api.events.message;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageSubscribe {
    boolean ignoreCancelled() default false;

    @Nullable String comment() default "";
}
