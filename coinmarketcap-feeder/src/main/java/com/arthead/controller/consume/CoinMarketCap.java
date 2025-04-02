package com.arthead.controller.consume;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class CoinMarketCap {
    public static void main(String[] args) throws IOException {
        String apiKey = args[0];
        Connection connection = Jsoup.connect("https://pro-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest")
                .data("slug", "bitcoin")
                .header("X-CMC_PRO_API_KEY", apiKey)
                .ignoreContentType(true)
                .method(Connection.Method.GET);
        connection.execute();
    }
}
