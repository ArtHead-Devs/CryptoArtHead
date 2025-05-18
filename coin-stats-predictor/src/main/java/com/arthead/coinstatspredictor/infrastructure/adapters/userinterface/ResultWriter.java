package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

public class ResultWriter {
    private final String csvPath;

    public ResultWriter(String csvPath) {
        this.csvPath = csvPath;
    }

    public void write(String content) throws IOException {
        Path path = Paths.get(csvPath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        try (FileWriter writer = new FileWriter(csvPath)) {
            writer.write(content);
        }
    }
}