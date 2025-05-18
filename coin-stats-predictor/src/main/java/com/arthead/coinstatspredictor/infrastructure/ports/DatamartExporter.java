package com.arthead.coinstatspredictor.infrastructure.ports;

import com.google.gson.JsonObject;

import java.util.List;

public interface DatamartExporter {
    void writeDatamart(List<JsonObject> data);
}
