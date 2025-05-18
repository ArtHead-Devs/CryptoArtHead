package com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor.rawdatawriter;

import com.arthead.coinstatspredictor.infrastructure.ports.CsvWriter;
import com.arthead.coinstatspredictor.util.CsvUtils;
import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;
import java.nio.file.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class GithubCsvWriter  implements CsvWriter {
    private static final String csvHeader = "timestamp,name,stars,forks,issues,watchers,owner,description,createDate,updateDate,pushDate";
    private final Path csvPath;
    private final Map<String, JsonObject> informationEvents = new HashMap<>();
    private final Map<String, JsonObject> repositoryEvents = new HashMap<>();
    private static final long maxDifference = 1500;

    public GithubCsvWriter(String csvPath) {
        this.csvPath = Paths.get(csvPath);
        CsvUtils.initializeFile(this.csvPath, csvHeader);
    }

    public synchronized void appendToCsv(JsonObject event) {
        try {
            String name = EventUtils.getSafeValue(event, "name");
            long ts = Instant.parse(event.get("ts").getAsString()).toEpochMilli();
            if (event.has("stars")) {
                tryMerge(informationEvents, name, ts, event);
                repositoryEvents.put(name, event);
            } else {
                tryMerge(repositoryEvents, name, ts, event);
                informationEvents.put(name, event);
            }
        } catch (Exception e) {
            System.err.println("Error processing GitHub event: " + e.getMessage());
        }
    }

    private void tryMerge(Map<String, JsonObject> targetMap, String name, long ts, JsonObject currentEvent) {
        JsonObject pairedEvent = targetMap.get(name);
        if (pairedEvent != null) {
            long pairedTs = Instant.parse(pairedEvent.get("ts").getAsString()).toEpochMilli();
            if (Math.abs(ts - pairedTs) <= maxDifference) {
                JsonObject merged = EventUtils.mergeJsonObjects(currentEvent, pairedEvent);
                CsvUtils.appendLine(csvPath, buildCsvLine(merged));
                targetMap.remove(name);
            }
        }
    }

    private String buildCsvLine(JsonObject data) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,\"%s\",%s,%s,%s",
                EventUtils.getSafeValue(data, "ts"),
                EventUtils.getSafeValue(data, "name"),
                EventUtils.getSafeValue(data, "stars"),
                EventUtils.getSafeValue(data, "forks"),
                EventUtils.getSafeValue(data, "issuesAndPullRequest"),
                EventUtils.getSafeValue(data, "watchers"),
                EventUtils.getSafeValue(data, "owner"),
                EventUtils.getSafeValue(data, "description").replace("\"", "\"\""),
                EventUtils.getSafeValue(data, "createDate"),
                EventUtils.getSafeValue(data, "updateDate"),
                EventUtils.getSafeValue(data, "pushDate")
        );
    }
}
