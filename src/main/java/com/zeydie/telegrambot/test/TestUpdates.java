package com.zeydie.telegrambot.test;

import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.subscribes.CancelableSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.PrioritySubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.telegram.events.update.UpdateEvent;
import com.zeydie.telegrambot.api.telegram.events.update.UpdateEventSubscribe;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@EventSubscribesRegister(comment = "Test update class")
public final class TestUpdates {
    @PrioritySubscribe(priority = EventPriority.LOWEST)
    @CancelableSubscribe
    @UpdateEventSubscribe
    public void lowest(@NotNull final UpdateEvent updateEvent) {
        log.debug("lowest {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.LOW)
    @UpdateEventSubscribe
    public void low(@NotNull final UpdateEvent updateEvent) {
        log.debug("low {}", updateEvent);

        //updateEvent.setCancelled(true);
    }

    @PrioritySubscribe(priority = EventPriority.DEFAULT)
    @UpdateEventSubscribe
    public void defaultA(@NotNull final UpdateEvent updateEvent) {
        log.debug("defaultA {}", updateEvent);
    }

    @UpdateEventSubscribe
    public void defaultB(@NotNull final UpdateEvent updateEvent) {
        log.debug("defaultB {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHT)
    @UpdateEventSubscribe
    public void hight(@NotNull final UpdateEvent updateEvent) {
        log.debug("hight {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHEST)
    @UpdateEventSubscribe
    public void highest(@NotNull final UpdateEvent updateEvent) {
        log.debug("highest {}", updateEvent);
    }
}