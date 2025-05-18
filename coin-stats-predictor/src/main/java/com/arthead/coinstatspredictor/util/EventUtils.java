package com.arthead.coinstatspredictor.util;

import com.google.gson.JsonObject;
import java.time.*;
import java.util.List;

public class EventUtils {
    public static int compareTimestamps(JsonObject a, JsonObject b) {
        Instant timestampA = Instant.parse(a.get("ts").getAsString());
        Instant timestampB = Instant.parse(b.get("ts").getAsString());
        return timestampA.compareTo(timestampB);
    }

    public static JsonObject findClosestEvent(List<JsonObject> events, Instant targetTime, Duration maxTimeGap) {
        JsonObject closestEvent = null;
        Duration smallestGap = maxTimeGap.plusSeconds(1);
        for (JsonObject event : events) {
            Instant eventTime = Instant.parse(event.get("ts").getAsString());
            Duration currentGap = Duration.between(eventTime, targetTime).abs();
            if (currentGap.compareTo(smallestGap) < 0) {
                smallestGap = currentGap;
                closestEvent = event;
            }
        }
        return smallestGap.compareTo(maxTimeGap) <= 0 ? closestEvent : null;
    }

    public static JsonObject mergeJsonObjects(JsonObject primary, JsonObject secondary) {
        JsonObject merged = new JsonObject();
        for (String key : primary.keySet()) {
            merged.add(key, primary.get(key));
        }
        for (String key : secondary.keySet()) {
            if (!merged.has(key)) {
                merged.add(key, secondary.get(key));
            }
        }
        return merged;
    }

    public static String getSafeValue(JsonObject obj, String key) {
        if (obj == null || !obj.has(key) || obj.get(key).isJsonNull()) {
            if (!key.equals("maxSupply")) {
                System.out.println("WARNING: Clave no encontrada o es null: " + key);
            }
            return "";
        }
        return obj.get(key).getAsString();
    }



    public static String sanitizeRawValue(String rawValue) {
        return rawValue != null
                ? rawValue.replace(",", "").replace("\"", "")
                : "";
    }
}
