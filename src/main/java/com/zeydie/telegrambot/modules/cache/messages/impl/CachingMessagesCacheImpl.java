package com.zeydie.telegrambot.modules.cache.messages.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.TelegramBotApp;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.ListMessagesData;
import com.zeydie.telegrambot.api.modules.cache.messages.data.MessageData;
import com.zeydie.telegrambot.utils.FileUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.zeydie.telegrambot.utils.ReferencePaths.CACHE_MESSAGES_FOLDER_PATH;

@Log4j2
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
                        final long chatId = notification.getKey();
                        @NotNull final ListMessagesData listMessagesData = notification.getValue();
                        @Nullable final List<MessageData> messageDatas = listMessagesData.messages();

                        log.debug("{} {}", chatId, Arrays.toString(Objects.requireNonNull(messageDatas).toArray()));

                        messageDatas.forEach(messageData -> TelegramBotApp.getMessageEventHandler().handle(messageData.message()));
                    }
            ).build();

    @SneakyThrows
    @Override
    public void load() {
        CACHE_MESSAGES_FOLDER_PATH.toFile().mkdirs();

        Arrays.stream(Objects.requireNonNull(CACHE_MESSAGES_FOLDER_PATH.toFile().listFiles()))
                .forEach(file -> {
                            try {
                                final long chatId = Long.parseLong(FileUtil.getFileName(file));
                                @NotNull final ListMessagesData listMessagesData = new SGsonFile(file).fromJsonToObject(new ListMessagesData(null));
                                @Nullable final List<MessageData> messages = listMessagesData.messages();

                                log.info(String.format("Chat: %d restored %d messages", chatId, messages.size()));

                                this.chatMessageCache.put(chatId, listMessagesData);
                            } catch (final Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                );
    }

    @Override
    public void save() {
        CACHE_MESSAGES_FOLDER_PATH.toFile().mkdirs();

        this.chatMessageCache.asMap().forEach((chat, message) -> {
                    new SGsonFile(CACHE_MESSAGES_FOLDER_PATH.resolve(String.valueOf(chat))).writeJsonFile(message);

                    log.info(String.format("Saving message cache for %d", chat));
                }
        );
    }

    @Override
    public void put(@NotNull final MessageData messageData) {
        final long id = messageData.message().chat().id();

        @Nullable ListMessagesData listMessagesData = this.chatMessageCache.getIfPresent(id);

        if (listMessagesData == null) {
            listMessagesData = new ListMessagesData(null);

            this.chatMessageCache.put(id, listMessagesData);
        }

        listMessagesData.add(messageData);
    }
}