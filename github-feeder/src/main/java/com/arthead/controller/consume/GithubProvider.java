package com.arthead.controller.consume;

import com.arthead.model.Repository;
import org.jsoup.Connection;

import java.util.List;

public class GithubProvider implements RepositoryProvider {
    private final GithubConnection connection;
    private final GithubFetcher fetcher;
    private final GithubDeserializer deserializer;

    public GithubProvider(GithubConnection connection, GithubFetcher fetcher, GithubDeserializer deserializer) {
        this.connection = connection;
        this.fetcher = fetcher;
        this.deserializer = deserializer;
    }

    @Override
    public List<Repository> provide() {
        try {
            Connection apiConnection = connection.createConnection();

            String json = fetcher.getResponseJson(apiConnection);

            Repository singleRepo = deserializer.deserialize(json);

            return List.of(singleRepo);

        } catch (Exception e) {
            System.err.println("Error al obtener la información del repositorio: " + e.getMessage());
            return List.of();
        }
    }
}
