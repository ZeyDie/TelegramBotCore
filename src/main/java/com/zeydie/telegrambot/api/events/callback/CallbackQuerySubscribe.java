package com.zeydie.telegrambot.api.events.callback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallbackQuerySubscribe {
    @NotNull String[] callbackDatas();

    @Nullable String comment() default "";
}
