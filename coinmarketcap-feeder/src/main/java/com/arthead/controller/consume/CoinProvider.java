package com.arthead.controller.consume;

import com.arthead.model.Coin;
import java.util.List;

public interface CoinProvider {
    List<Coin> provide();
}
