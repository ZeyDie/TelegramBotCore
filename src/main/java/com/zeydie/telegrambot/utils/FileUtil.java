package com.zeydie.telegrambot.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public final class FileUtil {
    public static @NotNull String createFileWithType(
            @NotNull final Object name,
            @NotNull final Object type
    ) {
        return name + "." + type;
    }

    public static @NotNull String getFileName(@NotNull final Path path) throws Exception {
        return getFileName(path.toFile());
    }

    public static @NotNull String getFileName(@NotNull final File file) throws Exception {
        return getFileName(file.getName());
    }

    public static @NotNull String getFileName(@NotNull final String fileName) throws Exception {
        return getFileNameOrType(fileName, false);
    }

    public static @NotNull String getFileType(@NotNull final Path path) throws Exception {
        return getFileType(path.toFile());
    }

    public static @NotNull String getFileType(@NotNull final File file) throws Exception {
        return getFileType(file.getName());
    }

    public static @NotNull String getFileType(@NotNull final String fileName) throws Exception {
        return getFileNameOrType(fileName, true);
    }

    public static @NotNull String getFileNameOrType(
            @NotNull final String fullFileName,
            final boolean type
    ) throws Exception {
        if (!fullFileName.contains(".")) throw new Exception("Bad file name!");

        @NotNull final String[] splitted = fullFileName.split("\\.");
        @NotNull final String fileName = splitted[0];
        @NotNull final String fileType = splitted[1];

        return type ? fileType : fileName;
    }
}