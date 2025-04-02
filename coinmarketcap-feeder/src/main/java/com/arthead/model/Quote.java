package com.arthead.model;

import java.util.Map;

public class Quote {
    private final Map<String, CurrencyInfo> currencies;

    public Quote(Map<String, CurrencyInfo> currencies) {
        this.currencies = currencies;
    }

    public Map<String, CurrencyInfo> getCurrencies() {
        return currencies;
    }
}
