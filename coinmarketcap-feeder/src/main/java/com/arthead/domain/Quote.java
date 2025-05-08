package com.arthead.domain;

public class Quote {
    private final String coin;
    private final String currency;
    private final Double price;
    private final Double volumeIn24h;
    private final Double volumeChange24h;
    private final Double percentChange1h;
    private final Double percentChange24h;
    private final Double percentChange7d;
    private final Double percentChange30d;
    private final Double percentChange60d;
    private final Double percentChange90d;
    private final Double marketCap;
    private final String ts;
    private final String ss ="CoinMarketCap";

    public Quote(String coin, String currency, Double price, Double volumeIn24h, Double volumeChange24h, Double percentChange1h, Double percentChange24h, Double percentChange7d, Double percentChange30d, Double percentChange60d, Double percentChange90d, Double marketCap, String ts) {
        this.coin = coin;
        this.currency = currency;
        this.price = price;
        this.volumeIn24h = volumeIn24h;
        this.volumeChange24h = volumeChange24h;
        this.percentChange1h = percentChange1h;
        this.percentChange24h = percentChange24h;
        this.percentChange7d = percentChange7d;
        this.percentChange30d = percentChange30d;
        this.percentChange60d = percentChange60d;
        this.percentChange90d = percentChange90d;
        this.marketCap = marketCap;
        this.ts = ts;
    }

    public String getCoin() {
        return coin;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getPrice() {
        return price;
    }

    public Double getVolumeIn24h() {
        return volumeIn24h;
    }

    public Double getVolumeChange24h() {
        return volumeChange24h;
    }

    public Double getPercentChange1h() {
        return percentChange1h;
    }

    public Double getPercentChange24h() {
        return percentChange24h;
    }

    public Double getPercentChange7d() {
        return percentChange7d;
    }

    public Double getPercentChange30d() {
        return percentChange30d;
    }

    public Double getPercentChange60d() {
        return percentChange60d;
    }

    public Double getPercentChange90d() {
        return percentChange90d;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public String getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
