package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.historicaleventprocessor;

import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class GithubInformationMerger {
    private static final Duration tolerance = Duration.ofMinutes(5);

    public List<JsonObject> mergeRepositoryWithInformation(Map<String, List<JsonObject>> information, Map<String,
            List<JsonObject>> repositories) {
        List<JsonObject> merged = new ArrayList<>();
        repositories.forEach((name, repoList) -> {
            List<JsonObject> infoList = information.get(name);
            if (infoList != null) mergeRepositoryAndInformationEvents(infoList, repoList, merged);
        });
        return merged;
    }

    private void mergeRepositoryAndInformationEvents(List<JsonObject> info, List<JsonObject> repos,
                                                     List<JsonObject> merged) {
        info.sort(EventUtils::compareTimestamps);
        repos.sort(EventUtils::compareTimestamps);
        for (JsonObject inf : info) {
            Instant ts = Instant.parse(inf.get("ts").getAsString());
            JsonObject repo = EventUtils.findClosestEvent(repos, ts, tolerance);
            if (repo != null) merged.add(EventUtils.mergeJsonObjects(inf, repo));
        }
    }
}
