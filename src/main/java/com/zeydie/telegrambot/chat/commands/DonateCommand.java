package com.zeydie.telegrambot.chat.commands;

import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.SendInvoice;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.utils.LanguageUtil;
import com.zeydie.telegrambot.api.utils.SendMessageUtil;
import com.zeydie.telegrambot.configs.ConfigStore;
import lombok.NonNull;
import lombok.val;

@EventSubscribesRegister
public final class DonateCommand {
    @CommandEventSubscribe(commands = "/donate")
    public void pay(@NonNull final CommandEvent event) {
        @NonNull val donateConfig = ConfigStore.getDonateConfig();

        if (!donateConfig.isEnabled()) return;

        val chatId = event.getSender().id();
        @NonNull val messages = event.getMessage().text().split(" ");

        var amount = donateConfig.getAmount();

        if (messages.length > 1)
            try {
                amount = Integer.parseInt(messages[1]);
            } catch (final Exception exception) {
                SendMessageUtil.sendMessage(
                        chatId,
                        LanguageUtil.localize(chatId, "messages.donate.no_parse")
                );

                return;
            }

        @NonNull val xtrPrice = new LabeledPrice(
                LanguageUtil.localize(chatId, "messages.donate.title"),
                amount
        );
        @NonNull val invoice = new SendInvoice(
                chatId,
                LanguageUtil.localize(chatId, "messages.donate.title"),
                LanguageUtil.localize(chatId, "messages.donate.description"),
                donateConfig.getPayload(),
                donateConfig.getCurrency(),
                xtrPrice
        );

        SendMessageUtil.execute(invoice);
    }
}