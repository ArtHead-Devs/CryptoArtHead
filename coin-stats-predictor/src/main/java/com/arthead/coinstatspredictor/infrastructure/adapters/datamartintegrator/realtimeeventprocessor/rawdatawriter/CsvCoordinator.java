package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter;

import com.arthead.coinstatspredictor.infrastructure.ports.CsvWriter;
import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class CsvCoordinator {
    private final Map<String, CsvWriter> writers = new HashMap<>();

    public CsvCoordinator(String coinCsvPath, String githubCsvPath) {
        registerWriter("CoinMarketCap", new CoinMarketCapCsvWriter(coinCsvPath));
        registerWriter("Github", new GithubCsvWriter(githubCsvPath));
    }

    public void registerWriter(String source, CsvWriter writer) {
        writers.put(source, writer);
    }

    public void writeEvent(JsonObject event) {
        String source = EventUtils.getSafeValue(event, "ss");
        CsvWriter writer = writers.get(source);
        if (writer != null) {
            writer.appendToCsv(event);
        } else {
            System.err.println("Unknow event. " + source);
        }
    }
}

