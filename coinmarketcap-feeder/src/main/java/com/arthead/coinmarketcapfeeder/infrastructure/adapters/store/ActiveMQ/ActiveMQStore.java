package com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.ActiveMQ;

import com.arthead.coinmarketcapfeeder.infrastructure.ports.CoinStore;
import com.arthead.coinmarketcapfeeder.domain.Coin;
import com.arthead.coinmarketcapfeeder.domain.Quote;
import jakarta.jms.JMSException;

import java.util.List;

public class ActiveMQStore implements CoinStore{
    private final CoinFeeder coinFeeder;
    private final QuoteFeeder quoteFeeder;

    public ActiveMQStore(String url) {
        this.coinFeeder = new CoinFeeder(url);
        this.quoteFeeder = new QuoteFeeder(url);
    }

    @Override
    public void save(List<Coin> coins, List<Quote> quotes){
        try {
            coinFeeder.sendCoins(coins);
            quoteFeeder.sendQuotes(quotes);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
