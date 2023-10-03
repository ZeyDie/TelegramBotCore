package com.zeydie.telegrambot.api.modules.cache.messages.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.pengrad.telegrambot.model.Message;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.modules.cache.messages.IMessagesCache;
import com.zeydie.telegrambot.api.modules.cache.messages.data.ChatMessagesData;
import com.zeydie.telegrambot.api.configs.AbstractFileConfig;
import com.zeydie.telegrambot.api.utils.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zeydie.telegrambot.api.utils.ReferencePaths.CACHE_MESSAGES_FOLDER;

@Log
public class CachingMessagesCacheImpl implements IMessagesCache {
    @NotNull
    private final Cache<Long, List<Message>> chatMessageCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.NANOSECONDS)
            .removalListener((RemovalListener<Long, List<Message>>) notification -> {
                final long chatId = notification.getKey();
                final List<Message> messages = notification.getValue();

                //TODO Processing message
            })
            .build();

    @SneakyThrows
    @Override
    public void load() {
        CACHE_MESSAGES_FOLDER.toFile().mkdirs();

        for (final File file : CACHE_MESSAGES_FOLDER.toFile().listFiles()) {
            final long chatId = Long.valueOf(FileUtil.getFileName(file));
            final ChatMessagesData chatMessagesData =  new SGsonFile(file).fromJsonToObject(new ChatMessagesData());

            final List<Message> messages = chatMessagesData.getMessages();

            log.info(String.format("Chat: %d restored %d messages", chatId, messages.size()));

            this.chatMessageCache.put(chatId, messages);
        }
    }

    @Override
    public void save() {
        CACHE_MESSAGES_FOLDER.toFile().mkdirs();

        this.chatMessageCache.asMap().forEach((chat, message) -> {
            final SGsonFile gsonFile = new SGsonFile(CACHE_MESSAGES_FOLDER.resolve(String.valueOf(chat)));
            final ChatMessagesData chatMessagesData = gsonFile.fromJsonToObject(new ChatMessagesData());

            chatMessagesData.getMessages().addAll(message);

            gsonFile.writeJsonFile(chatMessagesData);

            log.info(String.format("Saving message cache for %d", chat));
        });
    }

    @Override
    public void put(@NotNull final Message message) {
        final long id = message.chat().id();

        List<Message> messages = this.chatMessageCache.getIfPresent(id);

        if (messages == null) {
            messages = new ArrayList<>();

            this.chatMessageCache.put(id, messages);
        }

        messages.add(message);
    }
}
