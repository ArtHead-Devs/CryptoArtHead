package com.arthead.controller.consume;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.util.Map;

public class CoinMarketCapConnection {
    private final String apiKey;
    private final String endPoint;
    private final Map<String, String> queries;

    public CoinMarketCapConnection(String apiKey, String endPoint, Map<String, String> queries) {
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

    public Connection createConnection(){
        Connection connection = Jsoup.connect(getEndPoint())
                .data(getQueries())
                .header("X-CMC_PRO_API_KEY", getApiKey())
                .ignoreContentType(true)
                .method(Connection.Method.GET);

        return connection;
    }
}
