package com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.DatamartExporter;
import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class DatamartWriter implements DatamartExporter {
    private static final String[] HEADER = {
            "coin_name", "repository_name", "stars", "forks",
            "issuesAndPullRequest", "watchers", "currency", "price",
            "volumeChange24h", "percentChange1h", "percentChange24h",
            "percentChange7d", "percentChange30d", "percentChange60d",
            "percentChange90d", "coin_ts", "repository_ts"
    };

    private final Path outputPath;

    public DatamartWriter(String outputPath) {
        this.outputPath = Paths.get(outputPath);
    }

    @Override
    public void writeDatamart(List<JsonObject> data){
        try {
            initializeFile();
            for (JsonObject entry : data) {
                Files.write(outputPath, (convertToCSVLine(entry) + "\n").getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeFile() throws IOException {
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath.getParent());
            Files.write(outputPath, (String.join(",", HEADER) + "\n").getBytes());
        }
    }

    private String convertToCSVLine(JsonObject entry) {
        JsonObject coin = entry.getAsJsonObject("coin");
        JsonObject github = entry.getAsJsonObject("github");

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
