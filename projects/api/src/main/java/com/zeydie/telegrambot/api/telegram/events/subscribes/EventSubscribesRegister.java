package com.zeydie.telegrambot.api.telegram.events.subscribes;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribesRegister {
    boolean enable() default true;

    @Nullable String comment() default "";
}