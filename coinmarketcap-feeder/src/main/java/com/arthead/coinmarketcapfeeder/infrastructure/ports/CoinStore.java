package com.arthead.coinmarketcapfeeder.infrastructure.ports;

import com.arthead.coinmarketcapfeeder.domain.Coin;
import com.arthead.coinmarketcapfeeder.domain.Quote;
import java.util.List;

public interface CoinStore {
    void save(List<Coin> coins, List<Quote> quotes);
}
