package com.arthead.coinmarketcapfeeder.domain;

public class Coin {
    private final String name;
    private final String symbol;
    private final Long maxSupply;
    private final Long circulatingSupply;
    private final Long totalSupply;
    private final Boolean isActive;
    private final Boolean isFiduciary;
    private final Integer ranking;
    private final String ts;
    private final String ss = "CoinMarketCap";

    public Coin(String name, String symbol, Long maxSupply, Long circulatingSupply, Long totalSupply, Boolean isActive, Boolean isFiduciary, Integer ranking, String ts) {
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

    public Long getMaxSupply() {
        return maxSupply;
    }

    public Long getCirculatingSupply() {
        return circulatingSupply;
    }

    public Long getTotalSupply() {
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

    public String getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
