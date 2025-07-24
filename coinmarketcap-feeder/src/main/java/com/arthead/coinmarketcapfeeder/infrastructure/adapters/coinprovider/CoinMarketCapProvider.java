package com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider;

import com.arthead.coinmarketcapfeeder.domain.CoinMarketCapResponse;
import com.arthead.coinmarketcapfeeder.infrastructure.ports.CoinProvider;
import org.jsoup.Connection;

public class CoinMarketCapProvider implements CoinProvider {
    private final com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider.CoinMarketCapConnection connection;
    private final CoinMarketCapFetcher fetcher;
    private final com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider.CoinMarketCapDeserializer deserializer;

    public CoinMarketCapProvider(CoinMarketCapConnection connection, CoinMarketCapFetcher fetcher,
                                 CoinMarketCapDeserializer deserializer) {
        this.connection = connection;
        this.fetcher = fetcher;
        this.deserializer = deserializer;
    }

    @Override
    public CoinMarketCapResponse provide(){
        try {
            Connection httpConnection = connection.createConnection();
            String json = fetcher.fetcher(httpConnection);
            return deserializer.deserialize(json);
        } catch (Exception e) {
            throw new RuntimeException("Error in obtaining coins. ", e);
        }
    }
}
