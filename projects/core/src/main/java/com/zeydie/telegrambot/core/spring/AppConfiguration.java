package com.zeydie.telegrambot.core.spring;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.UpdatesListener;
import com.zeydie.telegrambot.api.Status;
import com.zeydie.telegrambot.api.handlers.events.language.ILanguageEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.payment.IPayment;
import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.api.telegram.events.handlers.ICallbackQueryEventHandler;
import com.zeydie.telegrambot.api.telegram.events.handlers.ICommandEventHandler;
import com.zeydie.telegrambot.api.telegram.events.handlers.IMessageEventHandler;
import com.zeydie.telegrambot.api.telegram.events.handlers.IUpdateEventHandler;
import com.zeydie.telegrambot.core.configs.ConfigStore;
import com.zeydie.telegrambot.core.configs.data.BotConfig;
import com.zeydie.telegrambot.core.configs.data.CachingConfig;
import com.zeydie.telegrambot.core.configs.data.DonateConfig;
import com.zeydie.telegrambot.core.configs.data.LanguageConfig;
import com.zeydie.telegrambot.core.impl.handlers.events.*;
import com.zeydie.telegrambot.core.impl.handlers.exceptions.ExceptionHandlerImpl;
import com.zeydie.telegrambot.core.impl.handlers.listeners.UpdatesListenerImpl;
import com.zeydie.telegrambot.core.impl.modules.cache.messages.CachingMessagesCacheImpl;
import com.zeydie.telegrambot.core.impl.modules.cache.messages.DirectlyMessagesCacheImpl;
import com.zeydie.telegrambot.core.impl.modules.cache.users.UserCacheImpl;
import com.zeydie.telegrambot.core.impl.modules.language.LanguageImpl;
import com.zeydie.telegrambot.core.impl.modules.payment.PaymentImpl;
import com.zeydie.telegrambot.core.impl.modules.permissions.UserPermissionsImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean("configStore")
    public @NotNull ConfigStore configStore() {
        return new ConfigStore();
    }

    @Bean("botConfig")
    public @NotNull BotConfig botConfig() {
        return this.configStore().getBotConfig();
    }

    @Bean("cachingConfig")
    public @NotNull CachingConfig cachingConfig() {
        return this.configStore().getCachingConfig();
    }

    @Bean("donateConfig")
    public @NotNull DonateConfig donateConfig() {
        return this.configStore().getDonateConfig();
    }

    @Bean("languageConfig")
    public @NotNull LanguageConfig languageConfig() {
        return this.configStore().getLanguageConfig();
    }

    @Bean("status")
    public @NotNull Status status() {
        return new Status();
    }

    @Bean("language")
    public @NotNull ILanguage language() {
        return new LanguageImpl();
    }

    @Bean("messagesCache")
    public @NotNull IMessagesCache messagesCache() {
        return ConfigStore.getCachingConfig().isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
    }

    @Bean("userCache")
    public @NotNull IUserCache userCache() {
        return new UserCacheImpl();
    }

    @Bean("payment")
    public @NotNull IPayment payment() {
        return new PaymentImpl();
    }

    @Bean("permissions")
    public @NotNull IPermissions permissions() {
        return new UserPermissionsImpl();
    }

    @Bean("languageEventHandler")
    public @NotNull ILanguageEventHandler languageEventHandler() {
        return new LanguageEventHandlerImpl();
    }

    @Bean("updateEventHandler")
    public @NotNull IUpdateEventHandler updateEventHandler() {
        return new UpdateEventHandlerImpl();
    }

    @Bean("callbackQueryEventHandler")
    public @NotNull ICallbackQueryEventHandler callbackQueryEventHandler() {
        return new CallbackQueryEventHandlerImpl();
    }

    @Bean("messageEventHandler")
    public @NotNull IMessageEventHandler messageEventHandler() {
        return new MessageEventHandlerImpl();
    }

    @Bean("commandEventHandler")
    public @NotNull ICommandEventHandler commandEventHandler() {
        return new CommandEventHandlerImpl();
    }

    @Bean("updatesListener")
    public @NotNull UpdatesListener updatesListener() {
        return new UpdatesListenerImpl();
    }

    @Bean("exceptionHandler")
    public @NotNull ExceptionHandler exceptionHandler() {
        return new ExceptionHandlerImpl();
    }
}