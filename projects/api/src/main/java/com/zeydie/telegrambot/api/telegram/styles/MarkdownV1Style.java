package com.zeydie.telegrambot.api.telegram.styles;

import com.pengrad.telegrambot.model.User;
import com.zeydie.telegrambot.api.modules.cache.users.data.UserData;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class MarkdownV1Style {
    public static @NotNull String bold(@NonNull final String text) {
        return "*" + text + "*";
    }

    public static @NotNull String italic(@NonNull final String text) {
        return "_" + text + "_";
    }

    public static @NotNull String inlineUrl(
            @NonNull final String text,
            @NonNull final URL url
    ) {
        return inlineUrl(text, url.toString());
    }

    public static @NotNull String inlineUrl(
            @NonNull final String text,
            @NonNull final String url
    ) {
        return "[" + text + "]" + "(" + url + ")";
    }

    public static @NotNull String inlineUser(
            @NonNull final String text,
            @NonNull final UserData userData
    ) {
        return inlineUser(text, userData.getUser().id());
    }

    public static @NotNull String inlineUser(
            @NonNull final String text,
            @NonNull final User user
    ) {
        return inlineUser(text, user.id());
    }

    public static @NotNull String inlineUser(
            @NonNull final String text,
            final long userId
    ) {
        return "[" + text + "]" + "(tg://user?id=" + userId + ")";
    }

    public static @NotNull String inlineFixedCode(@NonNull final String text) {
        return "`" + text + "`";
    }

    public static @NotNull String inlineCode(
            @NonNull final String language,
            @NonNull final String text
    ) {
        return "```" + language + "\n" + text + "\n```";
    }
}