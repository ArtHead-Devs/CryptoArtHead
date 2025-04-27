package com.arthead.model;

import java.time.Instant;

public class Coin {
    private final String name;
    private final String symbol;
    private final Integer maxSupply;
    private final Integer circulatingSupply;
    private final Integer totalSupply;
    private final Boolean isActive;
    private final Boolean isFiduciary;
    private final Integer ranking;
    private final Instant ts;
    private final String ss = "CoinMarketCap";

    public Coin(String name, String symbol, Integer maxSupply, Integer circulatingSupply, Integer totalSupply,
                Boolean isActive, Boolean isFiduciary, Integer ranking, Instant ts) {
        this.name = name;
        this.symbol = symbol;
        this.maxSupply = maxSupply;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
        this.isActive = isActive;
        this.isFiduciary = isFiduciary;
        this.ranking = ranking;
        this.ts = ts;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getMaxSupply() {
        return maxSupply;
    }

    public Integer getCirculatingSupply() {
        return circulatingSupply;
    }

    public Integer getTotalSupply() {
        return totalSupply;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Boolean getFiduciary() {
        return isFiduciary;
    }

    public Integer getRanking() {
        return ranking;
    }

    public Instant getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
