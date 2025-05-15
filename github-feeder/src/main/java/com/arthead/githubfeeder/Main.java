package com.arthead.githubfeeder;

import com.arthead.githubfeeder.application.usecases.collectorandstorer.Controller;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubConnection;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubDeserializer;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubFetcher;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubProvider;
import com.arthead.githubfeeder.infrastructure.adapters.store.ActiveMQ.ActiveMQStore;
import com.arthead.githubfeeder.infrastructure.ports.GithubRepositoryStore;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        List<Map<String, String>> repositories = List.of(
                Map.of("owner", "ethereum", "repo", "go-ethereum"),
                Map.of("owner", "XRPLF", "repo", "rippled"),
                Map.of("owner", "tronprotocol", "repo", "java-tron"),
                Map.of("owner", "IntersectMBO", "repo", "cardano-node"),
                Map.of("owner", "smartcontractkit", "repo", "chainlink"),
                Map.of("owner", "ava-labs", "repo", "avalanchego"),
                Map.of("owner", "stellar", "repo", "stellar-core"),
                Map.of("owner", "paritytech", "repo", "polkadot-sdk"),
                Map.of("owner", "dfinity", "repo", "ic"),
                Map.of("owner", "opentensor", "repo", "BitTensor")
        );

        System.out.println("=== SISTEMA DE ACTUALIZACIÓN DE REPOSITORIOS ===");

        GithubConnection connection = new GithubConnection(
                args[0],
                "https://api.github.com/repos/"
        );

        GithubProvider provider = new GithubProvider(
                connection,
                new GithubFetcher(),
                new GithubDeserializer()
        );

        GithubRepositoryStore store = new ActiveMQStore(args[1]);
        Controller controller = new Controller(provider, store, repositories);
        controller.execute();
    }
}

