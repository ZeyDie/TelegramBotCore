package com.zeydie.telegrambot.utils;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public final class FileUtil {
    public static @NotNull File createFileWithNameAndType(
            @NonNull final Path path,
            @NonNull final Object name,
            @NonNull final Object type
    ) {
        return path.resolve(createFileNameWithType(name, type)).toFile();
    }

    public static @NotNull File createFileWithName(
            @NonNull final Path path,
            @NonNull final Object name
    ) {
        return path.resolve(createFileNameWithoutType(name)).toFile();
    }

    public static @NotNull String createFileNameWithType(
            @NonNull final Object name,
            @NonNull final Object type
    ) {
        return name + "." + type;
    }

    public static @NotNull String createFileNameWithoutType(@NonNull final Object name) {
        return name.toString();
    }

    public static @NotNull String getFileName(@NonNull final Path path) throws Exception {
        return getFileName(path.toFile());
    }

    public static @NotNull String getFileName(@NonNull final File file) throws Exception {
        return getFileName(file.getName());
    }

    public static @NotNull String getFileName(@NonNull final String fileName) throws Exception {
        return getFileNameOrType(fileName, Type.FILENAME);
    }

    public static @NotNull String getFileType(@NonNull final Path path) throws Exception {
        return getFileType(path.toFile());
    }

    public static @NotNull String getFileType(@NonNull final File file) throws Exception {
        return getFileType(file.getName());
    }

    public static @NotNull String getFileType(@NonNull final String fileName) throws Exception {
        return getFileNameOrType(fileName, Type.FILETYPE);
    }

    public static @NotNull String getFileNameOrType(
            @NonNull final String fullFileName,
            @NonNull final Type type
    ) throws Exception {
        if (!fullFileName.contains(".")) return fullFileName;

        @NonNull val splitted = fullFileName.split("\\.");
        @NonNull val fileName = splitted[0];
        @NonNull val fileType = splitted[1];

        switch (type) {
            case FILENAME -> {
                return fileName;
            }
            case FILETYPE -> {
                return fileType;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static enum Type {
        FILENAME,
        FILETYPE;
    }
}