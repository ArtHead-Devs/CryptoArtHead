package com.arthead.model;

import java.util.List;

public class CoinMarketCapData {
    private final List<Coin> coins;
    private final List<Quote> quotes;

    public CoinMarketCapData(List<Coin> coins, List<Quote> quotes) {
        this.coins = coins;
        this.quotes = quotes;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }
}
