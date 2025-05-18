package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.historicaleventprocessor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventFileProcessor {
    public void processSingleEventFile(Path file, String keyField, Map<String, List<JsonObject>> groupedEvents) {
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
}

