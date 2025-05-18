package com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.CsvWriter;
import com.arthead.coinstatspredictor.util.CsvUtils;
import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CoinMarketCapCsvWriter implements CsvWriter {
    private static final String csvHeader = "timestamp,name,symbol,maxSupply,circulatingSupply,totalSupply,isActive,isFiduciary,ranking,coin,currency,price,volumeIn24h,volumeChange24h,percentChange1h,percentChange24h,percentChange7d,percentChange30d,percentChange60d,percentChange90d,marketCap";
    private final Path csvPath;
    private final Map<String, JsonObject> coinEvents = new HashMap<>();
    private final Map<String, JsonObject> quotesEvents = new HashMap<>();
    private static final long maxDifference = 1500;

    public CoinMarketCapCsvWriter(String csvPath) {
        this.csvPath = Paths.get(csvPath);
        CsvUtils.initializeFile(this.csvPath, csvHeader);
    }

    @Override
    public synchronized void appendToCsv(JsonObject event) {
        try {
            String name = event.has("name") ? EventUtils.getSafeValue(event, "name")
                    : EventUtils.getSafeValue(event, "coin");
            long tsMillis = Instant.parse(event.get("ts").getAsString()).toEpochMilli();
            routeEvent(name, tsMillis, event);
        } catch (Exception e) {
            System.err.println("Error processing event. " + e.getMessage());
        }
    }

    private void routeEvent(String name, long tsMillis, JsonObject event) {
        if (event.has("symbol") || event.has("name")) {
            tryMerge(quotesEvents, name, tsMillis, event, false);
            coinEvents.put(name + tsMillis, event);
        } else {
            tryMerge(coinEvents, name, tsMillis, event, true);
            quotesEvents.put(name + tsMillis, event);
        }
    }

    private void tryMerge(Map<String, JsonObject> target, String name, long ts, JsonObject event, boolean coinFirst) {
        target.entrySet().removeIf(entry -> processEntry(entry, name, ts, event, coinFirst));
    }

    private boolean processEntry(Map.Entry<String, JsonObject> entry, String name, long ts, JsonObject event, boolean coinFirst) {
        if (entry.getKey().startsWith(name)) {
            JsonObject paired = entry.getValue();
            long pairedTs = Instant.parse(paired.get("ts").getAsString()).toEpochMilli();
            if (Math.abs(ts - pairedTs) <= maxDifference) {
                JsonObject merged = EventUtils.mergeJsonObjects(coinFirst ? event : paired, coinFirst ? paired : event);
                CsvUtils.appendLine(csvPath, buildCsvLine(merged));
                return true;
            }
        }
        return false;
    }

    private String buildCsvLine(JsonObject data) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                EventUtils.getSafeValue(data, "ts"),
                EventUtils.getSafeValue(data, "name"),
                EventUtils.getSafeValue(data, "symbol"),
                EventUtils.getSafeValue(data, "maxSupply"),
                EventUtils.getSafeValue(data, "circulatingSupply"),
                EventUtils.getSafeValue(data, "totalSupply"),
                EventUtils.getSafeValue(data, "isActive"),
                EventUtils.getSafeValue(data, "isFiduciary"),
                EventUtils.getSafeValue(data, "ranking"),
                EventUtils.getSafeValue(data, "coin"),
                EventUtils.getSafeValue(data, "currency"),
                EventUtils.getSafeValue(data, "price"),
                EventUtils.getSafeValue(data, "volumeIn24h"),
                EventUtils.getSafeValue(data, "volumeChange24h"),
                EventUtils.getSafeValue(data, "percentChange1h"),
                EventUtils.getSafeValue(data, "percentChange24h"),
                EventUtils.getSafeValue(data, "percentChange7d"),
                EventUtils.getSafeValue(data, "percentChange30d"),
                EventUtils.getSafeValue(data, "percentChange60d"),
                EventUtils.getSafeValue(data, "percentChange90d"),
                EventUtils.getSafeValue(data, "marketCap")
        );
    }
}
