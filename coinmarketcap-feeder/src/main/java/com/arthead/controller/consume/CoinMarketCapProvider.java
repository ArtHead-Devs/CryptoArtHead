package com.arthead.controller.consume;

import com.arthead.model.Coin;
import org.jsoup.Connection;
import java.util.List;

public class CoinMarketCapProvider implements CoinProvider {
    private final CoinMarketCapConnection connection;
    private final CoinMarketCapFetcher fetcher;
    private final CoinMarketCapDeserializer deserializer;

    public CoinMarketCapProvider(CoinMarketCapConnection connection, CoinMarketCapFetcher fetcher, CoinMarketCapDeserializer deserializer) {
        this.connection = connection;
        this.fetcher = fetcher;
        this.deserializer = deserializer;
    }

    public CoinMarketCapConnection getConnection() {
        return connection;
    }

    public CoinMarketCapFetcher getFetcher() {
        return fetcher;
    }

    public CoinMarketCapDeserializer getDeserializer() {
        return deserializer;
    }

    @Override
    public List<Coin> provide(){
        try {
            Connection httpConnection = getConnection().createConnection();
            String json = getFetcher().fetcher(httpConnection);
            return getDeserializer().deserialize(json);
        } catch (Exception e) {
            System.err.println("Error al obtener las monedas: " + e.getMessage());
            return List.of();
        }
    }
}
