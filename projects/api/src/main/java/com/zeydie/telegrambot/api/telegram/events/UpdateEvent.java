package com.zeydie.telegrambot.api.telegram.events;

import com.pengrad.telegrambot.model.Update;
import com.zeydie.telegrambot.api.events.AbstractEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateEvent extends AbstractEvent {
    private final @NotNull Update update;
    private final boolean user;
    private final @Nullable Object sender;

    public UpdateEvent(@NonNull final Update update) {
        this.update = update;
        this.user = update.channelPost() == null;

        if (this.user) {
            @Nullable val message = update.message();

            this.sender = message == null ? null : message.from();
        } else {
            @Nullable val post = update.channelPost();

            this.sender = post == null ? null : post.senderChat();
        }
    }
}