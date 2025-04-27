package com.arthead.controller.broker;

import com.arthead.model.Coin;
import com.arthead.model.Quote;
import jakarta.jms.JMSException;

import java.util.List;

public class ActiveMQStore {
    private final CoinFeeder coinFeeder;
    private final QuoteFeeder quoteFeeder;

    public ActiveMQStore(CoinFeeder coinFeeder, QuoteFeeder quoteFeeder) {
        this.coinFeeder = coinFeeder;
        this.quoteFeeder = quoteFeeder;
    }

    public void save(List<Coin> coins, List<Quote> quotes){
        try {
            coinFeeder.sendCoins(coins);
            quoteFeeder.sendQuotes(quotes);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
