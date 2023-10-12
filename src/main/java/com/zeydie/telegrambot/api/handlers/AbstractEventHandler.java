package com.zeydie.telegrambot.api.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.subscribes.CancelableSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.events.subscribes.PrioritySubscribe;
import lombok.extern.log4j.Log4j2;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Log4j2
public abstract class AbstractEventHandler {
    @NotNull
    public abstract Class<? extends Annotation> getEventAnnotation();

    @Nullable
    public abstract Class<?>[] getParameters();

    private final @NotNull Cache<Method, Class<?>> highestClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> highClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> defaultClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> lowClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> lowestClassMethods = CacheBuilder.newBuilder().build();

    protected void load() {
        @NotNull final Class<? extends Annotation> eventAnnotation = this.getEventAnnotation();
        @Nullable final Class<?>[] eventParameters = this.getParameters();

        log.debug("Scanning events {}...", eventAnnotation);

        ClassIndex.getAnnotated(EventSubscribesRegister.class)
                .forEach(annotatedClass -> {
                            log.debug("{}", annotatedClass);

                            if (annotatedClass.getAnnotation(EventSubscribesRegister.class).enable())
                                Arrays.stream(annotatedClass.getMethods())
                                        .forEach(method -> {
                                                    if (method.isAnnotationPresent(eventAnnotation)) {
                                                        log.debug("{}", method);

                                                        @NotNull final Class<?>[] parameterTypes = method.getParameterTypes();

                                                        if (Arrays.equals(parameterTypes, eventParameters)) {
                                                            @NotNull final EventPriority eventPriority = method.isAnnotationPresent(PrioritySubscribe.class) ?
                                                                    method.getAnnotation(PrioritySubscribe.class).priority() :
                                                                    EventPriority.DEFAULT;

                                                            @Nullable Cache<Method, Class<?>> methodClassCache = null;

                                                            switch (eventPriority) {
                                                                case HIGHEST -> methodClassCache = this.highestClassMethods;
                                                                case HIGHT -> methodClassCache = this.highClassMethods;
                                                                case DEFAULT -> methodClassCache = this.defaultClassMethods;
                                                                case LOW -> methodClassCache = this.lowClassMethods;
                                                                case LOWEST -> methodClassCache = this.lowestClassMethods;
                                                            }

                                                            if (methodClassCache != null) {
                                                                methodClassCache.put(method, annotatedClass);
                                                                log.debug("{} == {}", parameterTypes, eventParameters);
                                                            }
                                                        }
                                                    }
                                                }
                                        );
                        }
                );
    }

    protected void invoke(@NotNull final Object... objects) {
        this.highestClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.highClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.defaultClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.lowClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.lowestClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
    }

    protected void invoke(
            @NotNull final Class<?> annotatedClass,
            @NotNull final Method method,
            @NotNull final Object... objects
    ) {
        final boolean canInvoke = !this.isCancelable(method) || !this.hasCancelledEvent(objects);

        if (canInvoke)
            try {
                method.invoke(annotatedClass.newInstance(), objects);
            } catch (
                    final IllegalAccessException |
                          InvocationTargetException |
                          InstantiationException exception
            ) {
                exception.printStackTrace();
            }
    }

    public boolean isCancelable(@NotNull final Method method) {
        return method.isAnnotationPresent(CancelableSubscribe.class);
    }

    public boolean hasCancelledEvent(@NotNull final Object... objects) {
        for (final Object object : objects)
            if (this.isCancelled(object))
                return true;

        return false;
    }

    public boolean isCancelled(@NotNull final Object object) {
        if (object instanceof AbstractEvent) {
            @NotNull final AbstractEvent abstractEvent = (AbstractEvent) object;

            if (abstractEvent.isCancelled())
                return true;
        }

        return false;
    }
}
