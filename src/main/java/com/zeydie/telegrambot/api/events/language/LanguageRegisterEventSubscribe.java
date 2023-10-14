package com.zeydie.telegrambot.api.events.language;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LanguageRegisterEventSubscribe {
    @Nullable String comment() default "";
}