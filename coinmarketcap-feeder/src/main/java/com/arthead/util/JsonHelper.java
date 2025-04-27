package com.arthead.util;

import com.google.gson.JsonObject;

public class JsonHelper {
    public static String getString(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull()
                ? obj.get(key).getAsString()
                : "";
    }

    public static Integer getInt(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull()
                ? obj.get(key).getAsInt()
                : null;
    }

    public static Double getDouble(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull()
                ? obj.get(key).getAsDouble()
                : null;
    }

    public static Boolean getBoolean(Integer value) {
        return value != null ? value == 1 : null;
    }
}
