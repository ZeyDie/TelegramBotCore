package com.zeydie.telegrambot.api.events.subscribes;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigSubscribesRegister {
    boolean enable() default true;

    @Nullable String comment() default "";
}