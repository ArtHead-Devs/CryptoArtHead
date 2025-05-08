package com.arthead.application.adapters.coinprovider;

import com.arthead.domain.Coin;
import com.arthead.domain.CoinMarketCapData;
import com.arthead.domain.Quote;
import com.arthead.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.*;

public class CoinMarketCapDeserializer {
    Gson gson = new Gson();

    public CoinMarketCapData deserialize(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        List<Coin> coins = parseCoins(jsonObject);
        List<Quote> quotes = parseQuotes(jsonObject);
        return new CoinMarketCapData(coins, quotes);
    }

    private List<Coin> parseCoins(JsonObject jsonObject) {
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        List<Coin> coins = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entryDataObject : dataObject.entrySet()) {
            JsonObject coinData = entryDataObject.getValue().getAsJsonObject();
            coins.add(parseCoin(coinData));
        }
        return coins;
    }

    private Coin parseCoin(JsonObject coinData) {
        String name = JsonHelper.getString(coinData, "name");
        String symbol = JsonHelper.getString(coinData, "symbol");
        Integer maxSupply = JsonHelper.getInt(coinData, "max_supply");
        Integer circulatingSupply = JsonHelper.getInt(coinData, "circulating_supply");
        Integer totalSupply = JsonHelper.getInt(coinData, "total_supply");
        Boolean isActive = JsonHelper.getBoolean(JsonHelper.getInt(coinData, "is_active"));
        Boolean isFiduciary = JsonHelper.getBoolean(JsonHelper.getInt(coinData, "is_fiat"));
        Integer ranking = JsonHelper.getInt(coinData, "cmc_rank");
        String ts = Instant.now().toString();

        return new Coin(name, symbol, maxSupply, circulatingSupply, totalSupply, isActive, isFiduciary,
                ranking, ts);
    }

    private List<Quote> parseQuotes(JsonObject jsonObject) {
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        List<Quote> quotes = new ArrayList<>();

        for (Map.Entry<String, JsonElement> coinEntry : dataObject.entrySet()) {
            JsonObject coinData = coinEntry.getValue().getAsJsonObject();
            String coinName = JsonHelper.getString(coinData, "name");
            JsonObject quoteObject = coinData.getAsJsonObject("quote");

            for (Map.Entry<String, JsonElement> currencyEntry : quoteObject.entrySet()) {
                String currency = currencyEntry.getKey();
                JsonObject quoteData = currencyEntry.getValue().getAsJsonObject();
                quotes.add(parseQuote(coinName, currency, quoteData));
            }
        }
        return quotes;
    }

    private Quote parseQuote(String coinName, String currency, JsonObject quoteObject) {
        Double price = JsonHelper.getDouble(quoteObject, "price");
        Double volumeIn24h = JsonHelper.getDouble(quoteObject, "volume_24h");
        Double volumeChange24h = JsonHelper.getDouble(quoteObject, "volume_change_24h");
        Double percentChange1h = JsonHelper.getDouble(quoteObject, "percent_change_1h");
        Double percentChange24h = JsonHelper.getDouble(quoteObject, "percent_change_24h");
        Double percentChange7d = JsonHelper.getDouble(quoteObject, "percent_change_7d");
        Double percentChange30d = JsonHelper.getDouble(quoteObject, "percent_change_30d");
        Double percentChange60d = JsonHelper.getDouble(quoteObject, "percent_change_60d");
        Double percentChange90d = JsonHelper.getDouble(quoteObject, "percent_change_90d");
        Double marketCap = JsonHelper.getDouble(quoteObject, "market_cap");
        String ts = Instant.now().toString();

        return new Quote(coinName, currency, price, volumeIn24h, volumeChange24h, percentChange1h, percentChange24h,
                percentChange7d, percentChange30d, percentChange60d, percentChange90d, marketCap, ts);
    }
}


