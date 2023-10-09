package com.zeydie.telegrambot.api.handlers;

import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.EventSubscribe;
import com.zeydie.telegrambot.api.events.PrioritySubscribe;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public abstract class AbstractEventHandler {
    @NotNull
    public abstract Class<? extends Annotation> getEventAnnotation();

    @Nullable
    public abstract Class<?>[] getParameters();

    @NotNull
    @Getter
    protected final Map<Method, Class<?>> classMethods = new HashMap<>();

    protected void load() {
        final Map<Method, Class<?>> methodClassMap = new HashMap<>();

        final Class<? extends Annotation> eventAnnotation = this.getEventAnnotation();
        final Class<?>[] eventParameters = this.getParameters();

        log.debug("Scanning {}...", eventAnnotation);

        ClassIndex.getAnnotated(EventSubscribe.class)
                .forEach(annotatedClass -> {
                            log.debug("{}", annotatedClass);

                            Arrays.stream(annotatedClass.getMethods())
                                    .forEach(method -> {
                                        if (method.isAnnotationPresent(eventAnnotation)) {
                                            log.debug("{}", method);

                                            final Class<?>[] parameterTypes = method.getParameterTypes();

                                            if (Arrays.equals(parameterTypes, eventParameters)) {
                                                methodClassMap.put(method, annotatedClass);
                                                log.debug("{} == {}", parameterTypes, eventParameters);
                                            }
                                        }
                                    });
                        }
                );

        methodClassMap
                .entrySet()
                .stream()
                .sorted((o1, o2) -> {
                    //TODO FIX Sorting
                    final Method methodO1 = o1.getKey();
                    final Method methodO2 = o2.getKey();

                    final PrioritySubscribe prioritySubscribeO1 = methodO1.isAnnotationPresent(PrioritySubscribe.class) ? methodO1.getAnnotation(PrioritySubscribe.class) : null;
                    final PrioritySubscribe prioritySubscribeO2 = methodO2.isAnnotationPresent(PrioritySubscribe.class) ? methodO2.getAnnotation(PrioritySubscribe.class) : null;

                    final EventPriority eventPriorityO1 = prioritySubscribeO1 != null ? prioritySubscribeO1.priority() : EventPriority.DEFAULT;
                    final EventPriority eventPriorityO2 = prioritySubscribeO2 != null ? prioritySubscribeO2.priority() : EventPriority.DEFAULT;

                    return -eventPriorityO2.compareTo(eventPriorityO1);
                })
                .forEach(methodClassEntry -> {
                    log.debug("sort {} {}", methodClassEntry.getKey(), methodClassEntry.getValue());
                    this.classMethods.put(methodClassEntry.getKey(), methodClassEntry.getValue());
                });
    }

    protected void invoke(
            @NotNull final Class<?> annotatedClass,
            @NotNull final Method method,
            @NotNull final Object... objects
    ) {
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
}
