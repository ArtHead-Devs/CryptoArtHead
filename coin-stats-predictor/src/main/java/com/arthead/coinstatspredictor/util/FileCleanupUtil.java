package com.arthead.coinstatspredictor.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileCleanupUtil {
    public static void deleteFiles(List<String> filePaths) {
        System.out.println("=== FILE CLEANUP ===");
        for (String pathStr : filePaths) {
            try {
                Path path = Path.of(pathStr);
                boolean deleted = Files.deleteIfExists(path);
                System.out.println(deleted
                        ? "✓ Deleted: " + pathStr
                        : "✗ Did not exist: " + pathStr);
            } catch (IOException e) {
                System.err.println("! Error deleting " + pathStr + ": " + e.getMessage());
            }
        }
    }
}
