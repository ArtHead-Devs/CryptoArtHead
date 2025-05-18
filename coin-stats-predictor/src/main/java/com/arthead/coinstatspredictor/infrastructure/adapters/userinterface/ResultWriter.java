package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {
    private final String csvPath;

    public ResultWriter(String csvPath) {
        this.csvPath = csvPath;
    }

    public void write(String content) throws IOException {
        try (FileWriter writer = new FileWriter(csvPath)) {
            writer.write(content);
        }
    }
}
