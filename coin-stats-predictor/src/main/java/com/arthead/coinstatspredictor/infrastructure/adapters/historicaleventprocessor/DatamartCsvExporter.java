package com.arthead.coinstatspredictor.infrastructure.adapters.historicaleventprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.DatamartExporter;
import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DatamartCsvExporter implements DatamartExporter {
    private final String outputCsvPath;
    private static final String[] header = {
            "coin_name", "repository_name", "stars", "forks", "issuesAndPullRequest", "watchers",
            "currency", "price", "volumeChange24h", "percentChange1h", "percentChange24h",
            "percentChange7d", "percentChange30d", "percentChange60d", "percentChange90d",
            "coin_ts", "repository_ts"
    };

    public DatamartCsvExporter(String outputCsvPath) {
        this.outputCsvPath = outputCsvPath;
    }

    @Override
    public void writeDatamart(List<JsonObject> data) {
        try {
            createOutputDirectoryIfNeeded();
            Files.write(Path.of(outputCsvPath), generateCsvLines(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Datamart exported successfully to " + outputCsvPath + " with " + data.size() + " records.");
    }

    private void createOutputDirectoryIfNeeded() throws IOException {
        Path parentDir = Path.of(outputCsvPath).getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
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
        JsonObject coin = item.getAsJsonObject("crypto");
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
