package com.arthead.coinstatspredictor.infrastructure.adapters.historicaleventprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.HistoricalEventLoader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
public class HistoricalEventReader implements HistoricalEventLoader {

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
                processSingleEventFile(file, groupingKey, groupedEvents);
            }
        } catch (IOException e) {
            System.err.println("Error processing files. " + e.getMessage());
        }
    }

    private void processSingleEventFile(Path file, String keyField, Map<String, List<JsonObject>> groupedEvents) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonObject event = JsonParser.parseString(line).getAsJsonObject();
                addEventToGroup(event, keyField, groupedEvents);
            }
        } catch (Exception e) {
            System.err.println("Error processing file " + file + ": " + e.getMessage());
        }
    }

    private void addEventToGroup(JsonObject event, String keyField, Map<String, List<JsonObject>> groupedEvents) {
        if (event.has(keyField) && !event.get(keyField).isJsonNull()) {
            String key = event.get(keyField).getAsString();
            if (!groupedEvents.containsKey(key)) {
                groupedEvents.put(key, new ArrayList<>());
            }
            groupedEvents.get(key).add(event);
        }
    }

    private String determineGroupingKey(String dirPath) {
        if (dirPath.contains("crypto.Quotes")) return "coin";
        else return "name";
    }
}
