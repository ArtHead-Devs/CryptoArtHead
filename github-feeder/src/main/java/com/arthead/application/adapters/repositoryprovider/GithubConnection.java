package com.arthead.application.adapters.repositoryprovider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class GithubConnection {
    private final String apiKey;
    private final String urlBase;

    public GithubConnection(String apiKey, String urlBase) {
        this.apiKey = apiKey;
        this.urlBase = urlBase;
    }

    public Connection createConnection(String owner, String repo) {
        String finalUrl = urlBase + owner + "/" + repo;
        return Jsoup.connect(finalUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Accept", "application/vnd.github+json")
                .ignoreContentType(true)
                .method(Connection.Method.GET);
    }
}