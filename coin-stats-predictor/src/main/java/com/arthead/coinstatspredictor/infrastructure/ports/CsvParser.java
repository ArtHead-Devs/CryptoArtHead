package com.arthead.coinstatspredictor.infrastructure.ports;

import com.google.gson.JsonObject;

import java.util.List;

public interface CsvParser {
    List<JsonObject> parse(List<String> lines);
}
