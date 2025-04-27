package com.arthead.controller.persistence;

import com.arthead.model.Coin;
import com.arthead.model.Quote;

public interface CoinStore {
    void save(Coin coin, Quote quote);
}
