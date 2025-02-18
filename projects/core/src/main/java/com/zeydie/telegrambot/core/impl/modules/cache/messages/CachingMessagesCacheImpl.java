package com.zeydie.telegrambot.core.impl.modules.cache.messages;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.ListMessagesData;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.api.telegram.events.handlers.IMessageEventHandler;
import com.zeydie.telegrambot.core.utils.FileUtil;
import com.zeydie.telegrambot.core.utils.LoggerUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.zeydie.telegrambot.core.utils.ReferencePaths.CACHE_MESSAGES_FOLDER_FILE;
import static com.zeydie.telegrambot.core.utils.ReferencePaths.CACHE_MESSAGES_FOLDER_PATH;

public class CachingMessagesCacheImpl implements IMessagesCache {
    @Autowired
    private IMessageEventHandler messageEventHandler;

    @Getter
    private final @NotNull Service scheduledService;

    public CachingMessagesCacheImpl() {
        this.scheduledService = new AbstractScheduledService() {
            @Override
            protected void runOneIteration() {
                chatMessageCache.cleanUp();
            }

            @Override
            protected @NotNull Scheduler scheduler() {
                return Scheduler.newFixedRateSchedule(Duration.ZERO, Duration.ofMillis(250));
            }
        }.startAsync();
    }

    private final @NotNull Cache<Long, ListMessagesData> chatMessageCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMillis(250))
            .removalListener((RemovalListener<Long, ListMessagesData>) notification -> {
                        if (notification.getKey() == null) return;
                        if (notification.getValue() == null) return;

                        @NonNull val chatId = notification.getKey();
                        @NonNull val listMessagesData = notification.getValue();
                        @NonNull val messageDatas = listMessagesData.getMessages();

                        LoggerUtil.debug("{} {}", chatId, Arrays.toString(messageDatas.toArray()));

                        messageDatas.forEach(
                                messageData -> {
                                    @Nullable val message = messageData.message();

                                    if (message != null)
                                        CompletableFuture.runAsync(() -> this.messageEventHandler.handle(message))
                                                .thenRun(() ->
                                                        SGsonFile.create(
                                                                        FileUtil.createFileWithName(
                                                                                CACHE_MESSAGES_FOLDER_PATH.resolve(String.valueOf(chatId)),
                                                                                FileUtil.createFileNameWithType(messageData.message().messageId(), "data")
                                                                        )
                                                                )
                                                                .writeJsonFile(messageData)
                                                );
                                }
                        );
                    }
            ).build();

    @Override
    public void preInit() {
        FileUtil.createFolder(CACHE_MESSAGES_FOLDER_FILE);
    }

    @Override
    public void init() {
        @Nullable val files = CACHE_MESSAGES_FOLDER_FILE.listFiles();

        if (files != null)
            Arrays.stream(files)
                    .forEach(
                            chatId -> {
                                try {
                                    @NonNull final ListMessagesData listMessagesData = new ListMessagesData();
                                    @NonNull final List<MessageData> messages = listMessagesData.getMessages();

                                    try (val stream = Files.walk(chatId.toPath())) {
                                        stream.forEachOrdered(
                                                message -> {
                                                    if (!Files.isDirectory(message))
                                                        messages.add(SGsonFile.create(message).fromJsonToObject(new MessageData(null)));
                                                }
                                        );
                                    }

                                    LoggerUtil.debug("Chat: {} restored {} messages", chatId, messages.size());
                                    this.chatMessageCache.put(Long.parseLong(FileUtil.getFileName(chatId)), listMessagesData);
                                } catch (final Exception exception) {
                                    LoggerUtil.error(exception);
                                }
                            }
                    );
    }

    @Override
    public void postInit() {
        this.chatMessageCache.cleanUp();
    }

    @Override
    public void save() {
    }

    @Override
    public void put(@NonNull final MessageData messageData) {
        val message = messageData.message();

        if (message == null) return;

        val chat = message.chat();

        if (chat == null) return;

        val id = chat.id();

        @Nullable var listMessagesData = this.chatMessageCache.getIfPresent(id);

        if (listMessagesData == null) {
            listMessagesData = new ListMessagesData();

            this.chatMessageCache.put(id, listMessagesData);
        }

        listMessagesData.add(messageData);
    }
}