package com.arthead.coinstatspredictor.infrastructure.ports;

import com.google.gson.JsonObject;

import java.util.List;

public interface DatamartWriter {
    void writeDatamart(List<JsonObject> data);
}
