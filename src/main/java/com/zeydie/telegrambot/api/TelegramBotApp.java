package com.zeydie.telegrambot.api;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.impl.CachingMessagesCacheImpl;
import com.zeydie.telegrambot.api.modules.cache.messages.impl.DirectlyMessagesCacheImpl;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.impl.UserCacheImpl;
import com.zeydie.telegrambot.api.configs.BotChatFileConfig;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
import com.zeydie.telegrambot.api.handlers.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.language.impl.MultiLanguage;
import com.zeydie.telegrambot.api.modules.language.impl.SingleLanguage;
import com.zeydie.telegrambot.api.listeners.impl.UpdatesListenerImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

public final class TelegramBotApp {
    @Getter
    private static String name;
    @Getter
    private static boolean chatingOlyUsers;

    @Getter
    private static ILanguage language;
    @Setter
    @Getter
    private static IMessagesCache messagesCache;
    @Setter
    @Getter
    private static IUserCache userCache;

    @Getter
    private static Status status = new Status();

    @Getter
    private static TelegramBot telegramBot;

    @SneakyThrows
    public static void setup(@NotNull final BotFileConfig.Json botFileConfig) {
        final BotChatFileConfig.Json botChatFileConfig = BotChatFileConfig.getJson();

        name = botFileConfig.getName();
        chatingOlyUsers = botChatFileConfig.isChatingOlyUsers();

        language = botChatFileConfig.isMultiLanguage() ? new MultiLanguage() : new SingleLanguage();
        messagesCache = botChatFileConfig.isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
        userCache = new UserCacheImpl();

        language.register(botChatFileConfig.getDefaultLanguageData());

        telegramBot = new TelegramBot(botFileConfig.getToken());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            messagesCache.save();
            userCache.save();
        }));
    }

    public static void init() {
        messagesCache.load();
        userCache.load();

        status.setUpdatingMessages(true);

        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);
    }

    @Setter
    private static UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    private static ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    @Data
    public static class Status {
        @NonFinal
        private boolean updatingMessages;
    }
}
