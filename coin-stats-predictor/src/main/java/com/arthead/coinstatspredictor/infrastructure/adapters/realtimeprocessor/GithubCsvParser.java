package com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor;

import com.arthead.coinstatspredictor.infrastructure.ports.CsvParser;
import com.arthead.coinstatspredictor.util.CsvUtils;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

public class GithubCsvParser implements CsvParser {
    private static final int expectedColumns = 11;
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
        obj.addProperty("stars", CsvUtils.sanitizeValue(values[2]));
        obj.addProperty("forks", CsvUtils.sanitizeValue(values[3]));
        obj.addProperty("issuesAndPullRequest", CsvUtils.sanitizeValue(values[4]));
        obj.addProperty("watchers", CsvUtils.sanitizeValue(values[5]));
        obj.addProperty("owner", CsvUtils.sanitizeValue(values[6]));
        obj.addProperty("description", CsvUtils.sanitizeValue(values[7]));
        obj.addProperty("createDate", CsvUtils.sanitizeValue(values[8]));
        obj.addProperty("updateDate", CsvUtils.sanitizeValue(values[9]));
        obj.addProperty("pushDate", CsvUtils.sanitizeValue(values[10]));
        
        return obj;
    }
}
