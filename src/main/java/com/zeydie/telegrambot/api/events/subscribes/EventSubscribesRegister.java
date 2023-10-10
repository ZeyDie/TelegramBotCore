package com.zeydie.telegrambot.api.events.subscribes;

import org.atteo.classindex.IndexAnnotated;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;

@IndexAnnotated
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribesRegister {
    boolean enable() default true;

    @Nullable String comment() default "";
}
