package com.zeydie.telegrambot.core.impl.chat.buttons;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.zeydie.telegrambot.core.TelegramBotCore;
import com.zeydie.telegrambot.api.telegram.events.CallbackQueryEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CallbackQueryEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.core.utils.LoggerUtil;
import com.zeydie.telegrambot.core.utils.SendMessageUtil;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventSubscribesRegister
public final class LanguageButton {
    public static @NotNull InlineKeyboardButton create(@Nullable final String text) {
        return new InlineKeyboardButton(text);
    }

    @CallbackQueryEventSubscribe(callbacks = "language.select", startWith = true)
    public void select(@NonNull final CallbackQueryEvent event) {
        @NonNull val datas = event.getCallbackQuery().data().split("\\.");
        val userId = Long.parseLong(datas[datas.length - 1]);
        @NonNull val languageCode = datas[datas.length - 2];

        @NonNull val userCache = TelegramBotCore.getInstance().getUserCache();

        if (!userCache.contains(userId)) {
            LoggerUtil.error(this.getClass(), "Can't find user {}", userId);
            return;
        }

        val userData = userCache.getUserData(userId);

        if (userData != null) {
            userData.setLanguageCode(languageCode);

            SendMessageUtil.sendMessage(userId, "messages.changed_language");
        } else
            SendMessageUtil.sendMessage(userId, "messages.no_user_data");
    }
}