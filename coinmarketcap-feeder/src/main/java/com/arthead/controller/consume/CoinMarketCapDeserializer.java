package com.arthead.controller.consume;

import com.arthead.model.Coin;
import com.arthead.model.CurrencyInfo;
import com.arthead.model.Quote;
import com.arthead.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinMarketCapDeserializer {
    Gson gson = new Gson();


    public List<Coin> deserialize(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        return parseCoins(dataObject);
    }

    private List<Coin> parseCoins(JsonObject dataObject) {
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

        JsonObject quoteObject = JsonHelper.getJsonObject(coinData, "quote");
        Quote quote = parseQuote(quoteObject);

        return new Coin(name, symbol, maxSupply, circulatingSupply, totalSupply, isActive, isFiduciary, ranking, quote);
    }

    private Quote parseQuote(JsonObject quoteObject) {
        Map<String, CurrencyInfo> currencies = new HashMap<>();
        if (quoteObject != null) {
            for (Map.Entry<String, JsonElement> entryQuoteObject : quoteObject.entrySet()) {
                String currency = entryQuoteObject.getKey();
                JsonObject currencyData = entryQuoteObject.getValue().getAsJsonObject();
                currencies.put(currency, parseCurrencyInfo(currencyData));
            }
        }
        return new Quote(currencies);
    }

    private CurrencyInfo parseCurrencyInfo(JsonObject currencyData) {
        return new CurrencyInfo(
                JsonHelper.getDouble(currencyData, "price"),
                JsonHelper.getDouble(currencyData, "volume_24h"),
                JsonHelper.getDouble(currencyData, "volume_change_24h"),
                JsonHelper.getDouble(currencyData, "percent_change_1h"),
                JsonHelper.getDouble(currencyData, "percent_change_24h"),
                JsonHelper.getDouble(currencyData, "percent_change_7d"),
                JsonHelper.getDouble(currencyData, "percent_change_30d"),
                JsonHelper.getDouble(currencyData, "percent_change_60d"),
                JsonHelper.getDouble(currencyData, "percent_change_90d"),
                JsonHelper.getDouble(currencyData, "market_cap")
        );
    }
}


