package com.arthead.application.ports;

import com.arthead.domain.Coin;
import com.arthead.domain.Quote;
import java.util.List;

public interface CoinStore {
    void save(List<Coin> coins, List<Quote> quotes);
}
