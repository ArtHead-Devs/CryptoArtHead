package com.arthead.controller.persistence;

import com.arthead.model.Coin;

public interface CoinStore {
    void save(Coin coin);
}
