package com.zeydie.telegrambot.test;

import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.subscribes.CancelableSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.PrioritySubscribe;
import com.zeydie.telegrambot.api.telegram.events.message.MessageEvent;
import com.zeydie.telegrambot.api.telegram.events.message.MessageEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@EventSubscribesRegister(comment = "Test message class")
public final class TestMessages {
    @PrioritySubscribe(priority = EventPriority.LOWEST)
    @CancelableSubscribe
    @MessageEventSubscribe
    public void lowest(@NotNull final MessageEvent messageEvent) {
        log.debug("lowest {}", messageEvent);
    }

    @PrioritySubscribe(priority = EventPriority.LOW)
    @MessageEventSubscribe
    public void low(@NotNull final MessageEvent messageEvent) {
        log.debug("low {}", messageEvent);

        messageEvent.setCancelled(true);
    }

    @PrioritySubscribe(priority = EventPriority.DEFAULT)
    @MessageEventSubscribe
    public void defaultA(@NotNull final MessageEvent messageEvent) {
        log.debug("defaultA {}", messageEvent);
    }

    @MessageEventSubscribe
    public void defaultB(@NotNull final MessageEvent messageEvent) {
        log.debug("defaultB {}", messageEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHT)
    @MessageEventSubscribe
    public void hight(@NotNull final MessageEvent messageEvent) {
        log.debug("hight {}", messageEvent);
    }

    @PrioritySubscribe(priority = EventPriority.HIGHEST)
    @MessageEventSubscribe
    public void highest(@NotNull final MessageEvent messageEvent) {
        log.debug("highest {}", messageEvent);
    }
}