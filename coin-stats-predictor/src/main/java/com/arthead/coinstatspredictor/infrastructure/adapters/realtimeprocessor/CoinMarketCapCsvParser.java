package com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.CsvParser;
import com.arthead.coinstatspredictor.util.CsvUtils;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

public class CoinMarketCapCsvParser implements CsvParser {
    private static final int expectedColumns = 21;
    private static final String csvSplitRegex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    public List<JsonObject> parse(List<String> lines) {
        return lines.stream()
            .filter(line -> !line.startsWith("timestamp"))
            .map(line -> parseLine(line.split(csvSplitRegex, -1)))
            .collect(Collectors.toList());
    }

    private JsonObject parseLine(String[] values) {
        JsonObject obj = new JsonObject();
        if (values.length < expectedColumns) return obj;

        obj.addProperty("ts", CsvUtils.sanitizeValue(values[0]));
        obj.addProperty("name", CsvUtils.sanitizeValue(values[1]));
        obj.addProperty("symbol", CsvUtils.sanitizeValue(values[2]));
        obj.addProperty("maxSupply", CsvUtils.sanitizeValue(values[3]));
        obj.addProperty("circulatingSupply", CsvUtils.sanitizeValue(values[4]));
        obj.addProperty("totalSupply", CsvUtils.sanitizeValue(values[5]));
        obj.addProperty("isActive", CsvUtils.sanitizeValue(values[6]));
        obj.addProperty("isFiduciary", CsvUtils.sanitizeValue(values[7]));
        obj.addProperty("ranking", CsvUtils.sanitizeValue(values[8]));
        obj.addProperty("coin", CsvUtils.sanitizeValue(values[9]));
        obj.addProperty("currency", CsvUtils.sanitizeValue(values[10]));
        obj.addProperty("price", CsvUtils.sanitizeValue(values[11]));
        obj.addProperty("volumeIn24h", CsvUtils.sanitizeValue(values[12]));
        obj.addProperty("volumeChange24h", CsvUtils.sanitizeValue(values[13]));
        obj.addProperty("percentChange1h", CsvUtils.sanitizeValue(values[14]));
        obj.addProperty("percentChange24h", CsvUtils.sanitizeValue(values[15]));
        obj.addProperty("percentChange7d", CsvUtils.sanitizeValue(values[16]));
        obj.addProperty("percentChange30d", CsvUtils.sanitizeValue(values[17]));
        obj.addProperty("percentChange60d", CsvUtils.sanitizeValue(values[18]));
        obj.addProperty("percentChange90d", CsvUtils.sanitizeValue(values[19]));
        obj.addProperty("marketCap", CsvUtils.sanitizeValue(values[20]));
        
        return obj;
    }
}
