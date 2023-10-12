package com.zeydie.telegrambot.test;

import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.subscribes.CancelableSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.events.subscribes.PrioritySubscribe;
import com.zeydie.telegrambot.api.events.update.UpdateEvent;
import com.zeydie.telegrambot.api.events.update.UpdateSubscribe;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@EventSubscribesRegister(comment = "Test update class")
public class TestUpdates {
    @PrioritySubscribe(priority = EventPriority.LOWEST)
    @CancelableSubscribe
    @UpdateSubscribe
    public void lowest(@NotNull final UpdateEvent updateEvent) {
        log.debug("lowest {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.LOW)
    @UpdateSubscribe
    public void low(@NotNull final UpdateEvent updateEvent) {
        log.debug("low {}", updateEvent);

        updateEvent.setCancelled(true);
    }

    @PrioritySubscribe(priority = EventPriority.DEFAULT)
    @UpdateSubscribe
    public void defaultA(@NotNull final UpdateEvent updateEvent) {
        log.debug("defaultA {}", updateEvent);
    }

    @UpdateSubscribe
    public void defaultB(@NotNull final UpdateEvent updateEvent) {
        log.debug("defaultB {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHT)
    @UpdateSubscribe
    public void hight(@NotNull final UpdateEvent updateEvent) {
        log.debug("hight {}", updateEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHEST)
    @UpdateSubscribe
    public void highest(@NotNull final UpdateEvent updateEvent) {
        log.debug("highest {}", updateEvent);
    }
}