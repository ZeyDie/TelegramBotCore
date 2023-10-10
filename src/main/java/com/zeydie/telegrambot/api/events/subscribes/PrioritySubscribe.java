package com.zeydie.telegrambot.api.events.subscribes;

import com.zeydie.telegrambot.api.events.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrioritySubscribe {
    @NotNull EventPriority priority() default EventPriority.DEFAULT;
}
