package com.zeydie.telegrambot.api.events;

import lombok.Data;

@Data
public abstract class AbstractEvent {
    private boolean cancelled;
}