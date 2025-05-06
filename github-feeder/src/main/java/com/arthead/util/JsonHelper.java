package com.arthead.util;

import com.google.gson.JsonObject;

public class JsonHelper {
    public static String getString(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull()
                ? obj.get(key).getAsString()
                : "";
    }
}
