package com.arthead.controller.consume;

import org.jsoup.Connection;

import java.io.IOException;

public class GithubFetcher {
    public String getResponseJson(Connection connection) throws IOException {
        Connection.Response response = connection.execute();
        if (response.statusCode() != 200) {
            throw new RuntimeException("Error HTTP: " + response.statusCode() + " - " + response.statusMessage());
        }
        return response.body();
    }
}
