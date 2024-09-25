package com.zeydie.telegrambot.api.telegram.styles;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HTMLStyle {
    public static final @NotNull String bold(@NonNull final String text) {
        return "<b>" + text + "</b>";
    }

    public static final @NotNull String italic(@NonNull final String text) {
        return "<i>" + text + "</i>";
    }

    public static final @NotNull String underline(@NonNull final String text) {
        return "<u>" + text + "</u>";
    }

    public static final @NotNull String strikethrough(@NonNull final String text) {
        return "<s>" + text + "</s>";
    }

    public static final @NotNull String spoiler(@NonNull final String text) {
        return "<tg-spoiler>" + text + "</tg-spoiler>";
    }

    public static final @NotNull String inlineUrl(
            @NonNull final String text,
            @NonNull final URL url
    ) {
        return inlineUrl(text, url.toString());
    }

    public static final @NotNull String inlineUrl(
            @NonNull final String text,
            @NonNull final String url
    ) {
        return "<a href=\"" + url + "\">" + url + "</a>";
    }

    public static final @NotNull String inlineUser(
            @NonNull final String text,
            @NonNull final UserData userData
    ) {
        return inlineUser(text, userData.getUser().id());
    }

    public static final @NotNull String inlineUser(
            @NonNull final String text,
            @NonNull final User user
    ) {
        return inlineUser(text, user.id());
    }

    public static final @NotNull String inlineUser(
            @NonNull final String text,
            final long userId
    ) {
        return "<a href=\"tg://user?id=" + userId + "\">" + text + "</a>";
    }

    public static final @NotNull String inlineFixedCode(@NonNull final String text) {
        return "<code>" + text + "</code>";
    }

    public static final @NotNull String inlineFixedCode(@NonNull final List<String> text) {
        return "<pre>"
                + text.stream().collect(Collectors.joining("\n")) +
                "</pre>";
    }

    public static final @NotNull String inlineCode(
            @Nullable final String language,
            @NonNull final String text
    ) {
        return "<code class=\"language-" + language + "\">" + text + "</code>";
    }

    public static final @NotNull String inlineCode(
            @Nullable final String language,
            @NonNull final List<String> text
    ) {
        return "<pre>" +
                "<code class=\"language-" + language + "\">"
                + text.stream().collect(Collectors.joining("\n")) +
                "</code>" +
                "</pre>";
    }

    public static final @NotNull String emoji(@NonNull final String emoji) {
        return "<tg-emoji>" + emoji + "</tg-emoji>";
    }

    public static final @NotNull String emojiOrSticker(
            @NonNull final String emoji,
            final long stickerId
    ) {
        return "<tg-emoji emoji-id=\"" + stickerId + "\">" + emoji + "</tg-emoji>";
    }

    public static final @NotNull String quotation(@NonNull final String text) {
        return quotations(Collections.singletonList(text));
    }

    public static final @NotNull String quotations(@NonNull final List<String> text) {
        return "<blockquote>" +
                text.stream()
                        .map(string -> string)
                        .collect(Collectors.joining("\n")) +
                "</blockquote>";
    }
}