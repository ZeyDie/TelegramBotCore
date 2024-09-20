package com.zeydie.telegrambot.modules.cache.messages.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.ListMessagesData;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.utils.FileUtil;
import com.zeydie.telegrambot.utils.LoggerUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.zeydie.telegrambot.utils.ReferencePaths.CACHE_MESSAGES_FOLDER_FILE;
import static com.zeydie.telegrambot.utils.ReferencePaths.CACHE_MESSAGES_FOLDER_PATH;

public class CachingMessagesCacheImpl implements IMessagesCache {
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
                return Scheduler.newFixedRateSchedule(0, 250, TimeUnit.MILLISECONDS);
            }
        }.startAsync();
    }

    private final @NotNull Cache<Long, ListMessagesData> chatMessageCache = CacheBuilder.newBuilder()
            .expireAfterWrite(100, TimeUnit.MILLISECONDS)
            .removalListener((RemovalListener<Long, ListMessagesData>) notification -> {
                        if (notification.getKey() == null) return;
                        if (notification.getValue() == null) return;

                        @NonNull val chatId = notification.getKey();
                        @NonNull val listMessagesData = notification.getValue();
                        @Nullable val messageDatas = listMessagesData.messages();

                        if (messageDatas != null)
                            LoggerUtil.debug("{} {}", chatId, Arrays.toString(messageDatas.toArray()));

                        messageDatas.forEach(
                                messageData -> {
                                    CompletableFuture.runAsync(() -> TelegramBotCore.getInstance().getMessageEventHandler().handle(messageData.message()))
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
        CACHE_MESSAGES_FOLDER_FILE.mkdirs();
    }

    @SneakyThrows
    @Override
    public void init() {
        @Nullable val files = CACHE_MESSAGES_FOLDER_FILE.listFiles();

        if (files != null)
            Arrays.stream(files)
                    .forEach(
                            chatId -> {
                                try {
                                    @NonNull final ListMessagesData listMessagesData = new ListMessagesData(null);
                                    @NonNull final List<MessageData> messages = listMessagesData.messages();

                                    Files.walk(chatId.toPath())
                                            .forEachOrdered(
                                                    message -> {
                                                        if (Files.isDirectory(message)) return;

                                                        messages.add(SGsonFile.create(message).fromJsonToObject(new MessageData(null)));
                                                    }
                                            );

                                    LoggerUtil.info("Chat: {} restored {} messages", chatId, messages.size());
                                    this.chatMessageCache.put(Long.parseLong(FileUtil.getFileName(chatId)), listMessagesData);
                                } catch (final Exception exception) {
                                    exception.printStackTrace();
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
        val id = messageData.message().chat().id();

        @Nullable var listMessagesData = this.chatMessageCache.getIfPresent(id);

        if (listMessagesData == null) {
            listMessagesData = new ListMessagesData(null);

            this.chatMessageCache.put(id, listMessagesData);
        }

        listMessagesData.add(messageData);
    }
}