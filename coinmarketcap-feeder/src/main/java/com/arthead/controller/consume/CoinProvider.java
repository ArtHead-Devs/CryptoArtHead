package com.arthead.controller.consume;

import com.arthead.model.CoinMarketCapData;

public interface CoinProvider {
    CoinMarketCapData provide();
}
