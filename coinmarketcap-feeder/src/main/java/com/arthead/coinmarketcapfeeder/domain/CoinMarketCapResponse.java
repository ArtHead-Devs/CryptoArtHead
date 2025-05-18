package com.arthead.coinmarketcapfeeder.domain;

import java.util.List;

public class CoinMarketCapResponse {
    private final List<Coin> coins;
    private final List<Quote> quotes;

    public CoinMarketCapResponse(List<Coin> coins, List<Quote> quotes) {
        this.coins = coins;
        this.quotes = quotes;
    }

    public List<Coin> getCoins() {
        return List.copyOf(coins);
    }

    public List<Quote> getQuotes() {
        return List.copyOf(quotes);
    }
}
