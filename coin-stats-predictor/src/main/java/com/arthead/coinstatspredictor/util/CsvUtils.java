package com.arthead.coinstatspredictor.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CsvUtils {
    public static String sanitizeValue(String value) {
        return (value != null)
                ? value.replace(",", "").replace("\"", "").trim()
                : "";
    }

    public static void initializeFile(Path path, String header) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.write(path, (header + "\n").getBytes());
            }
        } catch (IOException e) {
            System.err.println("Error initializing CSV file: " + e.getMessage());
        }
    }

    public static void appendLine(Path path, String line) {
        try {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, (line + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Error writing to " + path + ": " + e.getMessage());
        }
    }
}
