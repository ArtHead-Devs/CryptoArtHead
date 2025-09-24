package com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import java.io.IOException;

public class GithubFetcher {
    public String fetcher(Connection connection) throws IOException {
        Connection.Response response = connection.execute();
        if (response.statusCode() != 200) {
            throw new HttpStatusException("Error HTTP. ", response.statusCode(), response.url().toString());
        }
        return response.body();
    }
}
