package com.arthead.application.adapters.coinprovider;

import com.arthead.application.ports.CoinProvider;
import com.arthead.domain.CoinMarketCapData;
import org.jsoup.Connection;

public class CoinMarketCapProvider implements CoinProvider {
    private final CoinMarketCapConnection connection;
    private final CoinMarketCapFetcher fetcher;
    private final CoinMarketCapDeserializer deserializer;

    public CoinMarketCapProvider(CoinMarketCapConnection connection, CoinMarketCapFetcher fetcher,
                                 CoinMarketCapDeserializer deserializer) {
        this.connection = connection;
        this.fetcher = fetcher;
        this.deserializer = deserializer;
    }

    @Override
    public CoinMarketCapData provide(){
        try {
            Connection httpConnection = connection.createConnection();
            String json = fetcher.fetcher(httpConnection);
            return deserializer.deserialize(json);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las monedas. ", e);
        }
    }
}
