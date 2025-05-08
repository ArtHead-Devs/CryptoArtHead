package com.arthead.application.adapters.coinprovider;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import java.io.IOException;

public class CoinMarketCapFetcher {
    public String fetcher(Connection connection) throws IOException {
        Connection.Response response = connection.execute();
        if (response.statusCode() != 200) {
            throw new HttpStatusException("Error HTTP. ", response.statusCode(), response.url().toString());
        }
        return response.body();
    }
}