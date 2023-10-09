package com.zeydie.telegrambot.api.events;

import org.atteo.classindex.IndexAnnotated;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@IndexAnnotated
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribe {
    @Nullable String comment() default "";
}
