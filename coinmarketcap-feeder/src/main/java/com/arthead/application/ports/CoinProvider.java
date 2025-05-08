package com.arthead.application.ports;

import com.arthead.domain.CoinMarketCapData;

public interface CoinProvider {
    CoinMarketCapData provide();
}
