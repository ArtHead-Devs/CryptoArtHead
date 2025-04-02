package com.arthead;

import com.arthead.controller.consume.CoinMarketCapConnection;
import com.arthead.controller.consume.CoinMarketCapFetcher;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String apiKey = args[0];
        String endpoint = "https://pro-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest";
        Map<String, String> queries = new HashMap<>();
        queries.put("slug", "bitcoin");
        queries.put("convert", "USD");

        CoinMarketCapConnection connection = new CoinMarketCapConnection(apiKey, endpoint, queries);
        CoinMarketCapFetcher fetcher = new CoinMarketCapFetcher();
        String response = fetcher.fetcher(connection.createConnection());
        System.out.println(response);
    }
}