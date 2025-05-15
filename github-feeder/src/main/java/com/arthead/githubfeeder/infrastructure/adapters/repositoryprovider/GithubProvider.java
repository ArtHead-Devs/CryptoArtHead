package com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider;

import com.arthead.githubfeeder.infrastructure.ports.RepositoryProvider;
import com.arthead.githubfeeder.domain.GithubData;
import org.jsoup.Connection;

import java.util.Map;

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
    public GithubData provide(Map<String, String> repoQuery) {
        try {
            String owner = repoQuery.get("owner");
            String repo = repoQuery.get("repo");
            Connection connection = this.connection.createConnection(owner, repo);
            String json = fetcher.fetcher(connection);
            return deserializer.deserialize(json);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener repositorio. ", e);
        }
    }
}
