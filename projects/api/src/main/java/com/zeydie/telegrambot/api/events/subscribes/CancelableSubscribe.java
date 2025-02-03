package com.zeydie.telegrambot.api.events.subscribes;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CancelableSubscribe {
}