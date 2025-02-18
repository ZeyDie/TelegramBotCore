package com.zeydie.telegrambot.core.utils;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import com.zeydie.telegrambot.core.TelegramBotCore;
import lombok.NonNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class SendMessageUtil {
    private static TelegramBotCore telegramBotCore;

    @Autowired
    public SendMessageUtil(@NonNull final TelegramBotCore telegramBotCore) {
        SendMessageUtil.telegramBotCore = telegramBotCore;
    }

    public static void sendMessage(
            @NonNull final UserData userData,
            @NonNull final String message
    ) {
        sendMessage(
                userData.getUser(),
                message
        );
    }

    public static void sendMessage(
            @NonNull final User user,
            @NonNull final String message
    ) {
        sendMessage(
                user.id(),
                message
        );
    }

    public static void sendMessage(
            final long chatId,
            @NonNull final String message
    ) {
        execute(new SendMessage(chatId, message));
    }

    public static void sendMessage(
            @NonNull final UserData userData,
            @NonNull final String message,
            @NonNull final ParseMode parseMode
    ) {
        sendMessage(
                userData.getUser(),
                message,
                parseMode
        );
    }

    public static void sendMessage(
            @NonNull final User user,
            @NonNull final String message,
            @NonNull final ParseMode parseMode
    ) {
        sendMessage(
                user.id(),
                message,
                parseMode
        );
    }

    public static void sendMessage(
            final long chatId,
            @NonNull final String message,
            @NonNull final ParseMode parseMode
    ) {
        val sendMessage = new SendMessage(chatId, message);

        sendMessage.parseMode(parseMode);

        execute(sendMessage);
    }

    public static void execute(@NonNull final AbstractSendRequest<?> abstractSendRequest) {
        telegramBotCore.execute(abstractSendRequest);
    }
}