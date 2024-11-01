package com.zeydie.telegrambot.chat.commands;

import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.SendInvoice;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.utils.LanguageUtil;
import com.zeydie.telegrambot.configs.ConfigStore;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;

@EventSubscribesRegister
public final class DonateCommand {
    @SneakyThrows
    @CommandEventSubscribe(commands = "/donate")
    public void pay(@NonNull final CommandEvent event) {
        if (!ConfigStore.getDonateConfig().isEnabled()) return;

        @NonNull val instance = TelegramBotCore.getInstance();

        val chatId = event.getSender().id();
        @NonNull val messages = event.getMessage().text().split(" ");

        if (messages.length > 1) {
            int amount;

            try {
                amount = Integer.parseInt(messages[1]);
            } catch (final Exception exception) {
                //TODO Message that cant parse amount

                return;
            }

            @NonNull val donateConfig = ConfigStore.getDonateConfig();

            @NonNull val xtrPrice = new LabeledPrice(
                    LanguageUtil.localize(chatId, "messages.donate_title"),
                    amount
            );
            @NonNull val invoice = new SendInvoice(
                    chatId,
                    LanguageUtil.localize(chatId, "messages.donate_title"),
                    LanguageUtil.localize(chatId, "messages.donate_description"),
                    donateConfig.getPayload(),
                    donateConfig.getCurrency(),
                    xtrPrice
            );

            instance.execute(invoice);
        }
    }
}