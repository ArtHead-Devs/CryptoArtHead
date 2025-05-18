package com.arthead.coinstatspredictor.infrastructure.ports;

import com.google.gson.JsonObject;

public interface CsvWriter {
    void appendToCsv(JsonObject event);
}