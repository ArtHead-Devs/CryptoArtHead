package com.arthead.coinmarketcapfeeder.infrastructure.ports;

import com.arthead.coinmarketcapfeeder.domain.CoinMarketCapData;

public interface CoinProvider {
    CoinMarketCapData provide();
}
