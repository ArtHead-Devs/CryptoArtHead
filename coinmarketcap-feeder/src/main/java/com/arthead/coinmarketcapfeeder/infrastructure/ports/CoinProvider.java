package com.arthead.coinmarketcapfeeder.infrastructure.ports;

import com.arthead.coinmarketcapfeeder.domain.CoinMarketCapResponse;

public interface CoinProvider {
    CoinMarketCapResponse provide();
}
