package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.historicaleventprocessor;

import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CoinQuoteMerger {
    private static final Duration tolerance = Duration.ofMinutes(5);

    public List<JsonObject> mergeCoinWithQuotes(Map<String, List<JsonObject>> coinsEvents, Map<String,
            List<JsonObject>> quoteEvents) {
        List<JsonObject> mergedData = new ArrayList<>();
        coinsEvents.forEach((coinName, coinList) -> {
            List<JsonObject> quoteList = quoteEvents.get(coinName);
            if (quoteList != null) mergeCoinAndQuoteEvents(coinList, quoteList, mergedData);
        });
        return mergedData;
    }

    private void mergeCoinAndQuoteEvents(List<JsonObject> coinsEvents, List<JsonObject> quoteEvents,
                                         List<JsonObject> mergedData) {
        coinsEvents.sort(EventUtils::compareTimestamps);
        quoteEvents.sort(EventUtils::compareTimestamps);
        for (JsonObject coinEvent: coinsEvents) {
            Instant ts = Instant.parse(coinEvent.get("ts").getAsString());
            JsonObject quote = EventUtils.findClosestEvent(quoteEvents, ts, tolerance);
            if (quote != null) mergedData.add(EventUtils.mergeJsonObjects(coinEvent, quote));
        }
    }
}
