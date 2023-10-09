package com.zeydie.telegrambot.api.events.update;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateSubscribe {
    boolean ignoreCancelled() default false;

    @Nullable String comment() default "";
}
