package com.zeydie.telegrambot;

import com.pengrad.telegrambot.*;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import com.zeydie.telegrambot.api.handlers.events.language.ILanguageEventHandler;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.users.IUserCache;
import com.zeydie.telegrambot.api.modules.interfaces.ISubcore;
import com.zeydie.telegrambot.api.modules.language.ILanguage;
import com.zeydie.telegrambot.api.modules.permissions.IPermissions;
import com.zeydie.telegrambot.api.telegram.handlers.events.ICallbackQueryEventHandler;
import com.zeydie.telegrambot.api.telegram.handlers.events.ICommandEventHandler;
import com.zeydie.telegrambot.api.telegram.handlers.events.IMessageEventHandler;
import com.zeydie.telegrambot.api.telegram.handlers.events.IUpdateEventHandler;
import com.zeydie.telegrambot.configs.AbstractFileConfig;
import com.zeydie.telegrambot.configs.ConfigStore;
import com.zeydie.telegrambot.configs.data.BotConfig;
import com.zeydie.telegrambot.configs.data.CachingConfig;
import com.zeydie.telegrambot.exceptions.LanguageNotRegisteredException;
import com.zeydie.telegrambot.exceptions.SubcoreRegisteredException;
import com.zeydie.telegrambot.handlers.events.language.impl.LanguageEventHandlerImpl;
import com.zeydie.telegrambot.handlers.exceptions.impl.ExceptionHandlerImpl;
import com.zeydie.telegrambot.listeners.impl.UpdatesListenerImpl;
import com.zeydie.telegrambot.modules.cache.messages.impl.CachingMessagesCacheImpl;
import com.zeydie.telegrambot.modules.cache.messages.impl.DirectlyMessagesCacheImpl;
import com.zeydie.telegrambot.modules.cache.users.impl.UserCacheImpl;
import com.zeydie.telegrambot.modules.language.impl.LanguageImpl;
import com.zeydie.telegrambot.modules.permissions.local.UserPermissionsImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.CallbackQueryEventHandlerImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.CommandEventHandlerImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.MessageEventHandlerImpl;
import com.zeydie.telegrambot.telegram.handlers.events.impl.UpdateEventHandlerImpl;
import com.zeydie.telegrambot.utils.ReflectionUtil;
import com.zeydie.telegrambot.utils.RequestUtil;
import lombok.*;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public final class TelegramBotCore implements ISubcore {
    @Getter
    private static TelegramBotCore instance = new TelegramBotCore();

    @Getter
    private final @NotNull Status status = new Status();

    private final @NotNull Map<String, ISubcore> subcores = new HashMap<>();

    @Setter
    @Getter
    private ILanguage language;
    @Setter
    @Getter
    private IMessagesCache messagesCache;
    @Setter
    @Getter
    private IUserCache userCache;
    @Setter
    @Getter
    private IPermissions permissions;

    @Setter
    @Getter
    public @NotNull ILanguageEventHandler languageEventHandler = new LanguageEventHandlerImpl();

    @Setter
    @Getter
    private @NotNull IUpdateEventHandler updateEventHandler = new UpdateEventHandlerImpl();
    @Setter
    @Getter
    private @NotNull ICallbackQueryEventHandler callbackQueryEventHandler = new CallbackQueryEventHandlerImpl();
    @Setter
    @Getter
    private @NotNull IMessageEventHandler messageEventHandler = new MessageEventHandlerImpl();
    @Setter
    @Getter
    private @NotNull ICommandEventHandler commandEventHandler = new CommandEventHandlerImpl();

    @Getter
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

    @Override
    public void launch(@Nullable final String[] args) {
        System.setProperty("log4j.shutdownHookEnabled", Boolean.toString(false));

        log.debug("Scanning configs...");

        ClassIndex.getAnnotated(ConfigSubscribesRegister.class)
                .forEach(annotatedClass -> {
                            log.debug("{}", annotatedClass);

                            if (annotatedClass.getAnnotation(ConfigSubscribesRegister.class).enable()) {
                                @NotNull val annotatedClassInstance = ReflectionUtil.instance(annotatedClass);

                                Arrays.stream(annotatedClassInstance.getClass().getDeclaredFields())
                                        .forEach(field -> {
                                                    if (field.isAnnotationPresent(ConfigSubscribe.class)) {
                                                        @NotNull val configSubscribe = field.getAnnotation(ConfigSubscribe.class);

                                                        @NotNull val objectInstance = ReflectionUtil.instance(ReflectionUtil.getClassField(field));
                                                        @NotNull val config = !configSubscribe.file() ? objectInstance :
                                                                new AbstractFileConfig(
                                                                        Paths.get(configSubscribe.category().toString(), configSubscribe.path()),
                                                                        objectInstance,
                                                                        configSubscribe.name()
                                                                ).init();

                                                        ReflectionUtil.setValueField(field, annotatedClassInstance, config);
                                                    }
                                                }
                                        );
                            }
                        }
                );

        this.subcores.values().forEach(subcore -> subcore.launch(args));

        this.preInit();
        this.init();
        this.postInit();
    }

    @Override
    @SneakyThrows
    public void preInit() {
        setup(
                ConfigStore.getBotConfig(),
                ConfigStore.getCachingConfig()
        );
    }

    @SneakyThrows
    public void setup(
            @NonNull final BotConfig config,
            @NonNull final CachingConfig cachingConfig
    ) {
        final long startTime = System.currentTimeMillis();
        log.info("Starting setup...");

        this.languageEventHandler.preInit();
        this.updateEventHandler.preInit();
        this.callbackQueryEventHandler.preInit();
        this.messageEventHandler.preInit();
        this.commandEventHandler.preInit();

        this.language = new LanguageImpl();
        this.messagesCache = cachingConfig.isCaching() ? new CachingMessagesCacheImpl() : new DirectlyMessagesCacheImpl();
        this.userCache = new UserCacheImpl();
        this.permissions = new UserPermissionsImpl();

        this.language.preInit();
        this.messagesCache.preInit();
        this.userCache.preInit();
        this.permissions.preInit();

        this.telegramBot = new TelegramBot(config.getToken());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> stop()));

        this.subcores.values().forEach(subcore -> subcore.preInit());

        log.info("Setup's successful! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Override
    @SneakyThrows
    public void init() {
        final long startTime = System.currentTimeMillis();
        log.info("Starting initialize...");

        this.languageEventHandler.init();
        this.updateEventHandler.init();
        this.callbackQueryEventHandler.init();
        this.messageEventHandler.init();
        this.commandEventHandler.init();

        this.language.init();
        this.messagesCache.init();
        this.userCache.init();
        this.permissions.init();

        this.status.setUpdatingMessages(true);

        this.telegramBot.setUpdatesListener(this.updatesListener, this.exceptionHandler);

        this.subcores.values().forEach(subcore -> subcore.init());

        log.info("Initialized! ({} sec.)", ((System.currentTimeMillis() - startTime) / 1000.0));
    }

    @Override
    public void postInit() {
        this.languageEventHandler.postInit();
        this.updateEventHandler.postInit();
        this.callbackQueryEventHandler.postInit();
        this.messageEventHandler.postInit();
        this.commandEventHandler.postInit();

        this.language.postInit();
        this.messagesCache.postInit();
        this.userCache.postInit();
        this.permissions.postInit();

        this.subcores.values().forEach(subcore -> subcore.postInit());
    }

    @Override
    public void stop() {
        this.status.setUpdatingMessages(false);

        this.subcores.values().forEach(subcore -> subcore.stop());

        this.messagesCache.save();
        this.userCache.save();
        this.permissions.save();

        this.telegramBot.shutdown();
    }
    @Setter
    @Getter
    private @NotNull UpdatesListener updatesListener = new UpdatesListenerImpl();
    @Setter
    @Getter
    private @NotNull ExceptionHandler exceptionHandler = new ExceptionHandlerImpl();

    public void sendMessage(
            final long chatId,
            @NonNull final String message
    ) {
        this.execute(new SendMessage(chatId, message));
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
        @Nullable val text = (String) RequestUtil.getText(baseRequest);
        @Nullable val chatId = RequestUtil.getChatId(baseRequest);

        if (text != null)
            RequestUtil.setValue(
                    baseRequest,
                    RequestUtil.PARAMETER_TEXT,
                    chatId != null ? this.language.localizeObject(chatId, text) : this.language.localize(text)
            );

        @Nullable val keyboard = (Keyboard) RequestUtil.getKeyboard(baseRequest);

        if (keyboard != null) {
            if (keyboard instanceof @NonNull final InlineKeyboardMarkup inlineKeyboardMarkup) {
                Arrays.stream(inlineKeyboardMarkup.inlineKeyboard()).toList()
                        .forEach(inlineKeyboardButtons -> Arrays.stream(inlineKeyboardButtons).toList()
                                .forEach(inlineKeyboardButton -> {
                                            try {
                                                @NonNull val clazz = inlineKeyboardButton.getClass();
                                                @NonNull val buttonClass = clazz.getSuperclass() == Object.class ? clazz : clazz.getSuperclass();

                                                @NotNull val textInlineKeyboardField = buttonClass.getDeclaredField("text");
                                                @NotNull val textButton = inlineKeyboardButton.text();

                                                ReflectionUtil.setValueField(
                                                        textInlineKeyboardField,
                                                        inlineKeyboardButton,
                                                        this.language.localizeObject(chatId, textButton)
                                                );
                                            } catch (final NoSuchFieldException |
                                                           LanguageNotRegisteredException exception) {
                                                exception.printStackTrace();
                                            }
                                        }
                                )
                        );
            } else if (keyboard instanceof @NonNull final ReplyKeyboardMarkup replyKeyboardMarkup) {
                @NotNull val field = replyKeyboardMarkup.getClass().getDeclaredField("keyboard");
                @Nullable val replyKeyboardButtonsList = (List<List<KeyboardButton>>) ReflectionUtil.getValueField(field, replyKeyboardMarkup);

                if (replyKeyboardButtonsList != null)
                    replyKeyboardButtonsList
                            .forEach(replyKeyboardButtons -> replyKeyboardButtons
                                    .forEach(keyboardButton -> {
                                                try {
                                                    @NonNull val clazz = keyboardButton.getClass();
                                                    @NonNull val buttonClass = clazz.getSuperclass() == Object.class ? clazz : clazz.getSuperclass();

                                                    @NotNull val textKeyboardField = buttonClass.getDeclaredField("text");
                                                    @Nullable val textButton = (String) ReflectionUtil.getValueField(textKeyboardField, keyboardButton);

                                                    if (textButton != null)
                                                        ReflectionUtil.setValueField(
                                                                textKeyboardField,
                                                                keyboardButton,
                                                                this.language.localizeObject(chatId, textButton)
                                                        );
                                                } catch (final NoSuchFieldException |
                                                               LanguageNotRegisteredException exception) {
                                                    exception.printStackTrace();
                                                }
                                            }
                                    )
                            );
            } else throw new IllegalStateException("Unexpected value: " + keyboard);
        }

        if (baseRequest instanceof @NonNull SendMessage sendMessage)
            sendMessage.parseMode(ParseMode.Markdown);

        return baseRequest;
    }

    @Data
    public static class Status {
        @NonFinal
        private boolean updatingMessages;
    }
}