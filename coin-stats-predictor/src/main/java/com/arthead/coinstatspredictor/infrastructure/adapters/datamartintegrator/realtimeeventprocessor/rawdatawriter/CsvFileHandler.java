package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class CsvFileHandler {
    private final Path path;
    private long lineCount = 0;

    public CsvFileHandler(String filePath) {
        this.path = Paths.get(filePath);
        resetCounter();
    }

    public List<String> readAllLines() throws IOException {
        if (!Files.exists(path)) return Collections.emptyList();
        return Files.readAllLines(path);
    }

    public List<String> readNewLines() throws IOException {
        List<String> lines = readAllLines();
        return lines.subList((int) lineCount, lines.size());
    }

    public void updateLineCount(int newLines) {
        lineCount += newLines;
    }

    public void resetCounter() {
        lineCount = 0;
    }
}
