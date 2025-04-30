package com.arthead;

import com.arthead.controller.Controller;
import com.arthead.controller.consume.GithubConnection;
import com.arthead.controller.consume.GithubDeserializer;
import com.arthead.controller.consume.GithubFetcher;
import com.arthead.controller.consume.GithubProvider;
import com.arthead.controller.persistence.GithubRepositoryStore;
import com.arthead.controller.persistence.SQL.SQLiteStore;
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

        GithubRepositoryStore store = new SQLiteStore(args[1]);

        Controller controller = new Controller(provider, store, repositories);
        controller.execute();
    }
}

