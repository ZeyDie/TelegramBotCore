package com.zeydie.telegrambot.api.events;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrioritySubscribe {
    EventPriority priority() default EventPriority.DEFAULT;
}
