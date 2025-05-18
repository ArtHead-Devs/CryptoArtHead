package com.arthead.coinstatspredictor.infrastructure.ports;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public interface HistoricalEventLoader {
    Map<String, List<JsonObject>> loadAndGroupHistoricalEvents(String dirPath);
}
