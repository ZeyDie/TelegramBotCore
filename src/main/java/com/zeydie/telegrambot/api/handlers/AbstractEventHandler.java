package com.zeydie.telegrambot.api.handlers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.subscribes.CancelableSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.PrioritySubscribe;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import com.zeydie.telegrambot.api.telegram.events.CallbackQueryEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.telegram.events.CallbackQueryEvent;
import com.zeydie.telegrambot.telegram.events.CommandEvent;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public abstract class AbstractEventHandler implements IInitialize {
    public abstract @NotNull Class<? extends Annotation> getEventAnnotation();

    public abstract @Nullable Class<?>[] getParameters();

    private final @NotNull Cache<Method, Class<?>> highestClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> highClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> defaultClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> lowClassMethods = CacheBuilder.newBuilder().build();
    private final @NotNull Cache<Method, Class<?>> lowestClassMethods = CacheBuilder.newBuilder().build();

    @Override
    public void init() {
        @NonNull final val eventAnnotation = this.getEventAnnotation();
        @Nullable final val eventParameters = this.getParameters();

        log.debug("Scanning events {}...", eventAnnotation);

        ClassIndex.getAnnotated(EventSubscribesRegister.class)
                .forEach(annotatedClass -> {
                            log.debug("{}", annotatedClass);

                            if (annotatedClass.getAnnotation(EventSubscribesRegister.class).enable())
                                Arrays.stream(annotatedClass.getMethods())
                                        .forEach(method -> {
                                                    if (method.isAnnotationPresent(eventAnnotation)) {
                                                        log.debug("{}", method);

                                                        @NonNull final val parameterTypes = method.getParameterTypes();

                                                        if (Arrays.equals(parameterTypes, eventParameters)) {
                                                            @NonNull final val eventPriority = method.isAnnotationPresent(PrioritySubscribe.class) ?
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

    protected void invoke(@NonNull final Object... objects) {
        this.highestClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.highClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.defaultClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.lowClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
        this.lowestClassMethods.asMap().forEach((method, annotatedClass) -> invoke(annotatedClass, method, objects));
    }

    protected void invoke(
            @NonNull final Class<?> annotatedClass,
            @NonNull final Method method,
            @NonNull final Object... objects
    ) {
        final val canInvoke = (!this.isCancelable(method) || !this.hasCancelledEvent(objects));

        if (canInvoke) {
            if (method.isAnnotationPresent(CallbackQueryEventSubscribe.class) && !this.hasCallbackData(method, objects))
                return;
            if (method.isAnnotationPresent(CommandEventSubscribe.class) && !this.isCommand(method, objects)) return;

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

    public boolean isCancelable(@NonNull final Method method) {
        return method.isAnnotationPresent(CancelableSubscribe.class);
    }

    public boolean hasCancelledEvent(@NonNull final Object... objects) {
        for (final Object object : objects)
            if (this.isCancelled(object))
                return true;

        return false;
    }

    public boolean isCancelled(@NonNull final Object object) {
        return object instanceof final AbstractEvent abstractEvent && abstractEvent.isCancelled();
    }

    public boolean hasCallbackData(
            @NonNull final Method method,
            @NonNull final Object... objects
    ) {
        @Nullable final val callbackQueryEventSubscribe = method.getAnnotation(CallbackQueryEventSubscribe.class);
        @NonNull final val hasCallbackData = new AtomicBoolean(false);

        if (callbackQueryEventSubscribe != null)
            Arrays.stream(objects)
                    .forEach(object -> {
                                if (object instanceof final CallbackQueryEvent callbackQueryEvent) {
                                    @NonNull final val data = callbackQueryEvent.getCallbackQuery().data();

                                    hasCallbackData.set(
                                            Arrays.stream(callbackQueryEventSubscribe.callbackDatas())
                                                    .anyMatch(callbackData -> {
                                                                log.debug("Callback {}=?={}", data, callbackData);

                                                                if (callbackQueryEventSubscribe.startWith())
                                                                    return data.startsWith(callbackData);
                                                                else if (callbackQueryEventSubscribe.endWith())
                                                                    return data.endsWith(callbackData);
                                                                else
                                                                    return data.equals(callbackData);
                                                            }
                                                    )
                                    );
                                }
                            }
                    );

        return hasCallbackData.get();
    }

    public boolean isCommand(
            @NonNull final Method method,
            @NonNull final Object... objects
    ) {
        @Nullable final val commandEventSubscribe = method.getAnnotation(CommandEventSubscribe.class);
        @NonNull final val hasCommand = new AtomicBoolean(false);

        if (commandEventSubscribe != null)
            Arrays.stream(objects)
                    .forEach(object -> {
                                if (object instanceof final CommandEvent commandEvent) {
                                    @NonNull final val message = commandEvent.getMessage();
                                    @NonNull final val text = message.text();
                                    @NonNull final val chatId = message.from().id();
                                    @NonNull final val permissions = commandEventSubscribe.permissions();

                                    hasCommand.set(
                                            Arrays.stream(commandEventSubscribe.commands())
                                                    .anyMatch(command -> {
                                                                log.debug("Command {}=?={} {}", command, text, permissions);

                                                                @NonNull final val permissionsImpl = TelegramBotCore.getInstance().getPermissions();

                                                                return text.startsWith(command) && (
                                                                        permissions.length == 0 ||
                                                                                Arrays.stream(permissions).anyMatch(permission -> permissionsImpl.hasPermission(chatId, permission))
                                                                );
                                                            }
                                                    )
                                    );
                                }
                            }
                    );

        return hasCommand.get();
    }
}