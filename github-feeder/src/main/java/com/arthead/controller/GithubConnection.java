package com.arthead.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.Map;

public class GithubConnection {
    private final String apiKey;
    private final String endPoint;
    private final Map<String, String> queries;

    public GithubConnection(String apiKey, String endPoint, Map<String, String> queries) {
        this.apiKey = apiKey;
        this.endPoint = endPoint;
        this.queries = queries;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public Connection createConnection() {
        Connection connection = Jsoup.connect(getEndPoint())
                .data(getQueries())
                .header("Authorization", "Bearer " + getApiKey())
                .header("Accept", "application/vnd.github.v3+json")
                .ignoreContentType(true)
                .method(Connection.Method.GET);

        return connection;
    }
}