package com.arthead.eventstorebuilder.application.usecases.eventstorermanager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventStore {
    private static final String baseDir = "eventstore";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final Gson gson = new Gson();

    public void saveEvent(String topic, String json) {
        try {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            String ss = jsonObject.get("ss").getAsString();
            String ts = jsonObject.get("ts").getAsString();
            LocalDate date = LocalDate.parse(ts.substring(0, 10), DateTimeFormatter.ISO_DATE);
            File file = getEventFile(topic, ss, date);
            appendToFile(file, json);
        } catch (Exception e) {
            System.err.println("Error saving events: " + e.getMessage());
        }
    }

    private File getEventFile(String topic, String ss, LocalDate date) {
        String directoryPath = String.format("%s/%s/%s", baseDir, topic, ss);
        File directory = new File(directoryPath);
        if (!directory.exists()) directory.mkdirs();
        String fileName = date.format(dateTimeFormatter) + ".events";
        return new File(directory, fileName);
    }

    private void appendToFile(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(content);
            writer.newLine();
        } catch (Exception e) {
            throw new RuntimeException("Error writing in file: " + e.getMessage());
        }
    }
}
