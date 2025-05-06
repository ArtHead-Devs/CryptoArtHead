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

    public Connection createConnection(){
        return Jsoup.connect(endPoint)
                .data(queries)
                .header("X-CMC_PRO_API_KEY", apiKey)
                .ignoreContentType(true)
                .method(Connection.Method.GET);
    }
}