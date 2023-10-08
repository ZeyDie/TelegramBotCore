package com.zeydie.telegrambot.api;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import com.zeydie.telegrambot.api.configs.BotChatFileConfig;
import com.zeydie.telegrambot.api.configs.BotFileConfig;
import com.zeydie.telegrambot.api.handlers.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.api.listeners.impl.UpdatesListenerImpl;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.impl.CachingMessagesCacheImpl;
import com.zeydie.telegrambot.api.modules.cache.messages.impl.DirectlyMessagesCacheImpl;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.cache.users.impl.UserCacheImpl;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.language.impl.MultiLanguageImpl;
import com.zeydie.telegrambot.api.modules.language.impl.SingleLanguageImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Log4j2
public final class TelegramBotApp {
    @Getter
    private static String name;
    @Getter
    private static boolean chatingOlyUsers;

    @Setter
    @Getter
    private static ILanguage language;
    @Setter
    @Getter
    private static IMessagesCache messagesCache;
    @Setter
    @Getter
    private static IUserCache userCache;

    @Getter
    private static final Status status = new Status();

    @Getter
    private static TelegramBot telegramBot;

    @SneakyThrows
    public static void setup(@NotNull final BotFileConfig.Json botFileConfig) {
        final long startTime = System.currentTimeMillis();
        log.info("Starting setup...");

        final BotChatFileConfig.Json botChatFileConfig = BotChatFileConfig.getJson();

        name = botFileConfig.getName();
        chatingOlyUsers = botChatFileConfig.isChatingOlyUsers();

        language = botChatFileConfig.isMultiLanguage() ? new MultiLanguageImpl() : new SingleLanguageImpl();
        messagesCache = botChatFileConfig.isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
        userCache = new UserCacheImpl();

        language.init();

        telegramBot = new TelegramBot(botFileConfig.getToken());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            messagesCache.save();
            userCache.save();
        }));

        log.info("Setup's successful! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    public static void init() {
        final long startTime = System.currentTimeMillis();
        log.info("Starting initialize...");

        messagesCache.load();
        userCache.load();

        status.setUpdatingMessages(true);

        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);

        log.info("Initialized! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Setter
    private static @NotNull UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    private static @NotNull ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    public static @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(@NotNull final BaseRequest<T, R> baseRequest) {
        return telegramBot.execute(baseRequest);
    }

    @Data
    public static class Status {
        @NonFinal
        private boolean updatingMessages;
    }
}