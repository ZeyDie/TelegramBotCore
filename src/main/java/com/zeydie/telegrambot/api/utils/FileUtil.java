package com.zeydie.telegrambot.api.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public final class FileUtil {
    public static String createFileWithType(
            @NotNull final Object name,
            @NotNull final Object type
    ) {
        return name + "." + type;
    }

    public static String getFileName(@NotNull final Path path) throws Exception {
        return getFileName(path.toFile());
    }

    public static String getFileName(@NotNull final File file) throws Exception {
        return getFileName(file.getName());
    }

    public static String getFileName(@NotNull final String fileName) throws Exception {
        return getFileNameOrType(fileName, false);
    }

    public static String getFileType(@NotNull final Path path) throws Exception {
        return getFileType(path.toFile());
    }

    public static String getFileType(@NotNull final File file) throws Exception {
        return getFileType(file.getName());
    }

    public static String getFileType(@NotNull final String fileName) throws Exception {
        return getFileNameOrType(fileName, true);
    }

    public static String getFileNameOrType(@NotNull final String fullFileName, final boolean type) throws Exception {
        if (!fullFileName.contains(".")) throw new Exception("Bad file name!");

        final String[] splitted = fullFileName.split("\\.");

        final String fileName = splitted[0];
        final String fileType = splitted[1];

        return type ? fileType : fileName;
    }
}
