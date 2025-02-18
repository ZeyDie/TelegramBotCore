package com.zeydie.telegrambot.core;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.BaseResponse;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegrambot.api.RequestAPI;
import com.zeydie.telegrambot.api.Status;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import com.zeydie.telegrambot.api.handlers.events.language.ILanguageEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import com.zeydie.telegrambot.api.modules.interfaces.ISubcore;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.payment.IPayment;
import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.api.telegram.events.handlers.ICallbackQueryEventHandler;
import com.zeydie.telegrambot.api.telegram.events.handlers.ICommandEventHandler;
import com.zeydie.telegrambot.api.telegram.events.handlers.IMessageEventHandler;
import com.zeydie.telegrambot.api.telegram.events.handlers.IUpdateEventHandler;
import com.zeydie.telegrambot.core.configs.AbstractFileConfig;
import com.zeydie.telegrambot.core.configs.ConfigStore;
import com.zeydie.telegrambot.core.configs.data.BotConfig;
import com.zeydie.telegrambot.core.configs.data.CachingConfig;
import com.zeydie.telegrambot.core.utils.LoggerUtil;
import com.zeydie.telegrambot.core.utils.ReflectionUtil;
import com.zeydie.telegrambot.exceptions.language.LanguageNotRegisteredException;
import com.zeydie.telegrambot.exceptions.subcore.SubcoreRegisteredException;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class TelegramBotCore implements ISubcore {
    private static String[] arguments;
    private long startTime = System.currentTimeMillis();

    public static void main(@Nullable final String[] args) {
        arguments = args;
        SpringApplication.run(TelegramBotCore.class, arguments);
    }

    @Autowired
    private @NotNull ApplicationContext applicationContext;

    private final @NotNull Map<String, ISubcore> subcores = Maps.newHashMap();

    @Autowired
    private @NotNull Status status;

    @Autowired
    public @NotNull ILanguageEventHandler languageEventHandler;
    @Autowired
    private @NotNull IUpdateEventHandler updateEventHandler;
    @Autowired
    private @NotNull ICallbackQueryEventHandler callbackQueryEventHandler;
    @Autowired
    private @NotNull IMessageEventHandler messageEventHandler;
    @Autowired
    private @NotNull ICommandEventHandler commandEventHandler;

    @Autowired
    private @NotNull UpdatesListener updatesListener;
    @Autowired
    private @NotNull ExceptionHandler exceptionHandler;


    @Autowired
    private @NotNull ILanguage language;
    @Autowired
    private @NotNull IMessagesCache messagesCache;
    @Autowired
    private @NotNull IUserCache userCache;
    @Autowired
    private @NotNull IPayment payment;
    @Autowired
    private @NotNull IPermissions permissions;

    private final @NotNull Service saveScheduler = new AbstractScheduledService() {
        @Override
        protected void runOneIteration() throws Exception {
            CompletableFuture.runAsync(messagesCache::save)
                    .thenRun(userCache::save)
                    .thenRun(permissions::save);
        }

        @Override
        protected @NotNull Scheduler scheduler() {
            return Scheduler.newFixedRateSchedule(Duration.ofMinutes(0), Duration.ofMinutes(1));
        }
    };

    private TelegramBot telegramBot;

    public void registerSubcore(@NonNull final ISubcore subcore) throws SubcoreRegisteredException {
        @NonNull val key = subcore.getName();

        if (this.subcores.containsKey(key))
            throw new SubcoreRegisteredException(subcore);

        this.subcores.put(key, subcore);
    }

    @Override
    public @NotNull String getName() {
        return this.getClass().getName();
    }

    @PostConstruct
    public void launch() {
        System.setProperty("log4j.shutdownHookEnabled", Boolean.toString(false));

        LoggerUtil.debug("Scanning configs...");

        this.applicationContext.getBeansWithAnnotation(ConfigSubscribesRegister.class).values()
                .forEach(
                        annotatedClassInstance -> {
                            @NonNull val annotatedClass = annotatedClassInstance.getClass();

                            LoggerUtil.debug("ConfigSubscribesRegister {}", annotatedClass.getName());

                            if (annotatedClass.getAnnotation(ConfigSubscribesRegister.class).enable())
                                Arrays.stream(annotatedClassInstance.getClass().getDeclaredFields())
                                        .forEach(
                                                field -> {
                                                    if (field.isAnnotationPresent(ConfigSubscribe.class)) {
                                                        @NonNull val configSubscribe = field.getAnnotation(ConfigSubscribe.class);

                                                        @NonNull val objectInstance = ReflectionUtil.instance(ReflectionUtil.getClassField(field));
                                                        @NonNull val config = !configSubscribe.file() ? objectInstance :
                                                                new AbstractFileConfig(
                                                                        Paths.get(configSubscribe.category().toString(), configSubscribe.path()),
                                                                        objectInstance,
                                                                        configSubscribe.name()
                                                                ).init();

                                                        ReflectionUtil.setValueField(field, annotatedClassInstance, config);

                                                        LoggerUtil.debug(
                                                                "Init config {} {}",
                                                                configSubscribe.name(),
                                                                SGsonBase.create().fromObjectToJson(config)
                                                        );
                                                    }
                                                }
                                        );
                        }
                );

        this.launch(null);
    }

    @Override
    public void launch(@Nullable final String[] args) {
        LoggerUtil.info("Launching...");

        this.subcores.values().forEach(subcore -> subcore.launch(args));

        this.preInit();
        this.init();
        this.postInit();

        this.status.setStartup(false);

        LoggerUtil.info("Bot was launched in {} sec!", ((System.currentTimeMillis() - this.startTime) / 1000.0));
    }

    @Override
    public void preInit() {
        setup(
                ConfigStore.getBotConfig(),
                ConfigStore.getCachingConfig()
        );
    }

    public void setup(
            @NonNull final BotConfig config,
            @NonNull final CachingConfig cachingConfig
    ) {
        val startTime = System.currentTimeMillis();

        this.languageEventHandler.preInit();
        this.updateEventHandler.preInit();
        this.callbackQueryEventHandler.preInit();
        this.messageEventHandler.preInit();
        this.commandEventHandler.preInit();

        this.language.preInit();
        this.messagesCache.preInit();
        this.userCache.preInit();
        this.payment.preInit();
        this.permissions.preInit();

        this.telegramBot = new TelegramBot(config.getToken());

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        this.subcores.values().forEach(IInitialize::preInit);

        LoggerUtil.info("Setup's successful! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Override
    public void init() {
        val startTime = System.currentTimeMillis();

        this.languageEventHandler.init();
        this.updateEventHandler.init();
        this.callbackQueryEventHandler.init();
        this.messageEventHandler.init();
        this.commandEventHandler.init();

        this.language.init();
        this.messagesCache.init();
        this.userCache.init();
        this.payment.init();
        this.permissions.init();

        this.status.setUpdatingMessages(true);

        this.telegramBot.setUpdatesListener(this.updatesListener, this.exceptionHandler);

        this.subcores.values().forEach(IInitialize::init);

        LoggerUtil.info("Initialized! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Override
    public void postInit() {
        val startTime = System.currentTimeMillis();

        this.languageEventHandler.postInit();
        this.updateEventHandler.postInit();
        this.callbackQueryEventHandler.postInit();
        this.messageEventHandler.postInit();
        this.commandEventHandler.postInit();

        this.language.postInit();
        this.messagesCache.postInit();
        this.userCache.postInit();
        this.payment.postInit();
        this.permissions.postInit();

        this.subcores.values().forEach(IInitialize::postInit);

        this.saveScheduler.startAsync();

        LoggerUtil.info("Post initialized! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Override
    public void stop() {
        this.status.setUpdatingMessages(false);

        this.subcores.values().forEach(ISubcore::stop);

        this.messagesCache.save();
        this.userCache.save();
        this.permissions.save();

        this.telegramBot.removeGetUpdatesListener();
        this.telegramBot.shutdown();
    }

    public @Nullable File getFile(@NonNull final String fileId) {
        @NonNull val fileRequest = new GetFile(fileId);
        @Nullable val fileResponse = this.execute(fileRequest);

        if (fileResponse == null || !fileResponse.isOk()) return null;

        return fileResponse.file();
    }

    public @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(@NonNull final BaseRequest<T, R> baseRequest) {
        return this.telegramBot.execute(transforms(baseRequest));
    }

    public @Nullable <T extends BaseRequest<T, R>, R extends BaseResponse> Cancellable execute(
            @NonNull final T request,
            @NonNull final Callback<T, R> callback
    ) {
        return this.telegramBot.execute(request, callback);
    }

    @SneakyThrows
    public @NotNull <T extends BaseRequest<T, R>, R extends BaseResponse> BaseRequest<T, R> transforms(@NonNull final BaseRequest<T, R> baseRequest) {
        @Nullable val text = (String) RequestAPI.getText(baseRequest);
        @Nullable val chatId = RequestAPI.getChatId(baseRequest);

        if (text != null)
            RequestAPI.setValue(
                    baseRequest,
                    RequestAPI.PARAMETER_TEXT,
                    chatId != null ? this.language.localizeObject(chatId, text) : this.language.localize(text)
            );

        @Nullable val keyboard = (Keyboard) RequestAPI.getKeyboard(baseRequest);

        if (keyboard != null) {
            if (keyboard instanceof @NonNull final InlineKeyboardMarkup inlineKeyboardMarkup) {
                Arrays.stream(inlineKeyboardMarkup.inlineKeyboard()).toList()
                        .forEach(inlineKeyboardButtons -> Arrays.stream(inlineKeyboardButtons).toList()
                                .forEach(inlineKeyboardButton -> {
                                            try {
                                                @NonNull val clazz = inlineKeyboardButton.getClass();
                                                @NonNull val buttonClass = clazz.getSuperclass() == Object.class ? clazz : clazz.getSuperclass();

                                                @NonNull val textInlineKeyboardField = buttonClass.getDeclaredField("text");
                                                @Nullable val textButton = inlineKeyboardButton.text();

                                                if (textButton != null)
                                                    ReflectionUtil.setValueField(
                                                            textInlineKeyboardField,
                                                            inlineKeyboardButton,
                                                            this.language.localizeObject(chatId, textButton)
                                                    );
                                            } catch (final NoSuchFieldException | LanguageNotRegisteredException exception) {
                                                LoggerUtil.error(exception);
                                            }
                                        }
                                )
                        );
            } else if (keyboard instanceof @NonNull final ReplyKeyboardMarkup replyKeyboardMarkup) {
                @NonNull val field = replyKeyboardMarkup.getClass().getDeclaredField("keyboard");
                @Nullable val replyKeyboardButtonsList = (List<List<KeyboardButton>>) ReflectionUtil.getValueField(field, replyKeyboardMarkup);

                if (replyKeyboardButtonsList != null)
                    replyKeyboardButtonsList
                            .forEach(replyKeyboardButtons -> replyKeyboardButtons
                                    .forEach(
                                            keyboardButton -> {
                                                try {
                                                    @NonNull val clazz = keyboardButton.getClass();
                                                    @NonNull val buttonClass = clazz.getSuperclass() == Object.class ? clazz : clazz.getSuperclass();

                                                    @NonNull val textKeyboardField = buttonClass.getDeclaredField("text");
                                                    @Nullable val textButton = (String) ReflectionUtil.getValueField(textKeyboardField, keyboardButton);

                                                    if (textButton != null)
                                                        ReflectionUtil.setValueField(
                                                                textKeyboardField,
                                                                keyboardButton,
                                                                this.language.localizeObject(chatId, textButton)
                                                        );
                                                } catch (final NoSuchFieldException |
                                                               LanguageNotRegisteredException exception) {
                                                    LoggerUtil.error(exception);
                                                }
                                            }
                                    )
                            );
            } else throw new IllegalStateException("Unexpected value: " + keyboard);
        }

        return baseRequest;
    }
}