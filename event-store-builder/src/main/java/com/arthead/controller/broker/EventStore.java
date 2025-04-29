// EventStore.java
package com.arthead.controller.broker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventStore {
    private static final String base_dir = "eventstore";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final Gson gson = new Gson();

    public void saveEvent(String topic, String json) {
        try {
            JsonObject obj = gson.fromJson(json, JsonObject.class);
            String ss = obj.get("ss").getAsString();
            String ts = obj.get("ts").getAsString();
            LocalDate date = LocalDate.parse(ts.substring(0, 10));

            String path = String.format("%s/%s/%s/%s.events",
                    base_dir, topic, ss, date.format(dateTimeFormatter));
            File file = new File(path);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(json);
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error guardando evento: " + e.getMessage());
        }
    }
}
