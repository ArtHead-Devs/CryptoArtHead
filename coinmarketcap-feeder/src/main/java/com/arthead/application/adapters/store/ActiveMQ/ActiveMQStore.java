package com.arthead.application.adapters.store.ActiveMQ;

import com.arthead.application.ports.CoinStore;
import com.arthead.domain.Coin;
import com.arthead.domain.Quote;
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
