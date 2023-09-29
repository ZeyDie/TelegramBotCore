package com.zeydie.telegrambot.api.cache.chat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pengrad.telegrambot.model.Message;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegrambot.api.configs.AbstractFileConfig;
import com.zeydie.telegrambot.api.data.cache.ChatMessagesData;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log
public class CacheUpdates {
    @NotNull
    private final Cache<Long, List<Message>> chatMessageCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    @SneakyThrows
    public void init() {
        final File cacheFolder = AbstractFileConfig.CACHE_FOLDER.toFile();

        if (!cacheFolder.exists())
            cacheFolder.mkdirs();

        for (final File file : cacheFolder.listFiles()) {
            final SGsonFile gsonFile = new SGsonFile(file);
            final ChatMessagesData chatMessagesData = gsonFile.fromJsonToObject(new ChatMessagesData());

            final long id = Long.parseLong(file.getName().split("\\.")[0]);
            final List<Message> messages = chatMessagesData.getMessages();

            log.info(String.format("Chat: %d restored %d messages", id, messages.size()));

            this.chatMessageCache.put(id, messages);
        }
    }

    public void shutdown() {
        this.chatMessageCache.asMap().forEach((chat, message) -> {
            final SGsonFile gsonFile = new SGsonFile(AbstractFileConfig.CACHE_FOLDER.resolve(String.valueOf(chat)));
            final ChatMessagesData chatMessagesData = gsonFile.fromJsonToObject(new ChatMessagesData());

            chatMessagesData.getMessages().addAll(message);

            gsonFile.writeJsonFile(chatMessagesData);

            log.info(String.format("Saving message cache for %d", chat));
        });
    }

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
