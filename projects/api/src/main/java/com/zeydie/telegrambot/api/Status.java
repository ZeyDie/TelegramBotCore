package com.zeydie.telegrambot.api;

import lombok.Data;
import lombok.experimental.NonFinal;

@Data
public class Status {
    @NonFinal
    private boolean updatingMessages;
    private boolean startup = true;
}