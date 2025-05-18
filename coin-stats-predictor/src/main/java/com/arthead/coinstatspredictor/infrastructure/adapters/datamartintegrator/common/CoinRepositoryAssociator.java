package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.common;

import com.arthead.coinstatspredictor.util.EventUtils;
import com.google.gson.JsonObject;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CoinRepositoryAssociator {
    private static final Duration tolerance = Duration.ofHours(5);
    private static final Map<String, String> coinRepositoryAssociations = Map.of(
            "Ethereum", "go-ethereum", "XRP", "rippled", "TRON", "java-tron",
            "Cardano", "cardano-node", "Chainlink", "chainlink", "Avalanche", "avalanchego",
            "Stellar", "stellar-core", "Polkadot", "polkadot-sdk", "Internet Computer", "ic",
            "Bittensor", "bittensor"
    );

    public List<JsonObject> associateCoinsWithRepositories(List<JsonObject> coinEvents,
                                                           List<JsonObject> repositoryEvents) {
        Map<String, List<JsonObject>> repositoriesByName = groupRepositoriesByName(repositoryEvents);
        List<JsonObject> associations = new ArrayList<>();
        for (JsonObject coinEvent : coinEvents) {
            processSingleCoinEvent(coinEvent, repositoriesByName, associations);
        }
        return associations;
    }

    private void processSingleCoinEvent(JsonObject coinEvent,Map<String, List<JsonObject>> repositoriesByName,
                                        List<JsonObject> associations) {
        String coinName = coinEvent.get("name").getAsString();
        String repoName =  coinRepositoryAssociations.get(coinName);
        if (repoName != null && repositoriesByName.containsKey(repoName)) {
            Instant eventTime = Instant.parse(coinEvent.get("ts").getAsString());
            List<JsonObject> repositoriesEvents = repositoriesByName.get(repoName);
            JsonObject closestRepoEvent = EventUtils.findClosestEvent(repositoriesEvents, eventTime, tolerance);

            if (closestRepoEvent != null) {
                associations.add(createCoinRepositoryAssociation(coinEvent, closestRepoEvent));
            }
        }
    }

    private Map<String, List<JsonObject>> groupRepositoriesByName(List<JsonObject> repositories) {
        Map<String, List<JsonObject>> grouped = new HashMap<>();
        for (JsonObject repository : repositories) {
            String name = repository.get("name").getAsString();
            if (!grouped.containsKey(name)) {
                grouped.put(name, new ArrayList<>());
            }
            grouped.get(name).add(repository);
        }
        return grouped;
    }

    private JsonObject createCoinRepositoryAssociation(JsonObject coin, JsonObject repository) {
        JsonObject association = new JsonObject();
        association.add("coin", coin);
        association.add("github", repository);
        return association;
    }
}



