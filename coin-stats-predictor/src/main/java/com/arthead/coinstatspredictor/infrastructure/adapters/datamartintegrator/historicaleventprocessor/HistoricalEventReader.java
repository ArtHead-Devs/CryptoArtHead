package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.historicaleventprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.HistoricalEventLoader;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
public class HistoricalEventReader implements HistoricalEventLoader {
    private final EventFileProcessor eventFileProcessor;

    public HistoricalEventReader() {
        this.eventFileProcessor = new EventFileProcessor();
    }

    @Override
    public Map<String, List<JsonObject>> loadAndGroupHistoricalEvents(String directoryPath) {
        Map<String, List<JsonObject>> groupedEvents = new HashMap<>();
        Path directory = Paths.get(directoryPath);
        if (!Files.exists(directory) && Files.isDirectory(directory)) {
            return groupedEvents;
        }
        String groupingKey = determineGroupingKey(directoryPath);
        processAllEventFiles(directory, groupingKey, groupedEvents);
        return groupedEvents;
    }

    private void processAllEventFiles(Path directory, String groupingKey, Map<String, List<JsonObject>> groupedEvents) {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(directory, "*.events")) {
            for (Path file : files) {
                eventFileProcessor.processSingleEventFile(file, groupingKey, groupedEvents);
            }
        } catch (IOException e) {
            System.err.println("Error processing files. " + e.getMessage());
        }
    }

    private String determineGroupingKey(String dirPath) {
        if (dirPath.contains("crypto.Quotes")) return "coin";
        else return "name";
    }
}
