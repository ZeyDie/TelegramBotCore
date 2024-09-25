package com.zeydie.telegrambot.api.telegram.styles;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class MarkdownV2Style extends MarkdownV1Style {
    public static final @NotNull String underline(@NonNull final String text) {
        return "__" + text + "__";
    }

    public static final @NotNull String strikethrough(@NonNull final String text) {
        return "~" + text + "~";
    }

    public static final @NotNull String spoiler(@NonNull final String text) {
        return "||" + text + "||";
    }

    public static final @NotNull String emoji(@NonNull final String emoji) {
        return "![" + emoji + "]";
    }

    public static final @NotNull String emojiOrSticker(
            @NonNull final String emoji,
            final long stickerId
    ) {
        return "![" + emoji + "](tg://emoji?id=" + stickerId + ")";
    }

    public static final @NotNull String quotation(@NonNull final String text) {
        return ">" + text;
    }

    public static final @NotNull String quotations(@NonNull final List<String> text) {
        return text.stream()
                .map(string -> quotation(string))
                .collect(Collectors.joining());
    }
}