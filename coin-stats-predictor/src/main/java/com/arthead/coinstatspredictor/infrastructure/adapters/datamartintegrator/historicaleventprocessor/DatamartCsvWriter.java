package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.historicaleventprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.DatamartWriter;
import com.arthead.coinstatspredictor.util.CsvUtils;
import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DatamartCsvWriter implements DatamartWriter {
    private final Path outputCsvPath;
    private static final String[] header = {
            "coin_name", "repository_name", "stars", "forks", "issuesAndPullRequest", "watchers",
            "currency", "price", "volumeChange24h", "percentChange1h", "percentChange24h",
            "percentChange7d", "percentChange30d", "percentChange60d", "percentChange90d",
            "coin_ts", "repository_ts"
    };

    public DatamartCsvWriter(String outputCsvPath) {
        this.outputCsvPath = Path.of(outputCsvPath);
    }

    @Override
    public void writeDatamart(List<JsonObject> data) {
        try {
            String headerString = String.join(",", header);
            CsvUtils.initializeFile(outputCsvPath, headerString);
            Files.write(outputCsvPath, generateCsvLines(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Datamart exported successfully to " + outputCsvPath + " with " + data.size() + " records.");
    }

    private List<String> generateCsvLines(List<JsonObject> data) {
        List<String> csvLines = new ArrayList<>();
        csvLines.add(String.join(",", header));
        for (JsonObject item : data) {
            csvLines.add(buildCsvDataLine(item));
        }
        return csvLines;
    }

    private String buildCsvDataLine(JsonObject item) {
        JsonObject coin = item.getAsJsonObject("coin");
        JsonObject github = item.getAsJsonObject("github");
        return String.join(",",
                EventUtils.getSafeValue(coin, "name"),
                EventUtils.getSafeValue(github, "name"),
                EventUtils.getSafeValue(github, "stars"),
                EventUtils.getSafeValue(github, "forks"),
                EventUtils.getSafeValue(github, "issuesAndPullRequest"),
                EventUtils.getSafeValue(github, "watchers"),
                EventUtils.getSafeValue(coin, "currency"),
                EventUtils.getSafeValue(coin, "price"),
                EventUtils.getSafeValue(coin, "volumeChange24h"),
                EventUtils.getSafeValue(coin, "percentChange1h"),
                EventUtils.getSafeValue(coin, "percentChange24h"),
                EventUtils.getSafeValue(coin, "percentChange7d"),
                EventUtils.getSafeValue(coin, "percentChange30d"),
                EventUtils.getSafeValue(coin, "percentChange60d"),
                EventUtils.getSafeValue(coin, "percentChange90d"),
                EventUtils.getSafeValue(coin, "ts"),
                EventUtils.getSafeValue(github, "ts")
        );
    }
}
