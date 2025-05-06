package com.arthead.controller.persistence;

import com.arthead.model.Coin;
import com.arthead.model.Quote;
import java.util.List;

public interface CoinStore {
    void save(List<Coin> coins, List<Quote> quotes);
}
