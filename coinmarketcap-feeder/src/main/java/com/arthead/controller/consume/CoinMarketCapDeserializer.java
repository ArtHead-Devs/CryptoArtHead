package com.arthead.controller.consume;

import com.arthead.model.Coin;
import com.arthead.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoinMarketCapDeserializer {
    Gson gson = new Gson();

    public List<Coin> deserialize(String json) {
        List<Coin> coins = new ArrayList<>();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject dataObject = jsonObject.getAsJsonObject("data");

        for (Map.Entry<String, JsonElement> set : dataObject.entrySet()) {
            JsonObject coinData = set.getValue().getAsJsonObject();

            String name = JsonHelper.getString(coinData, "name");
            String symbol = JsonHelper.getString(coinData, "symbol");
            Integer maxSupply = JsonHelper.getInt(coinData, "max_supply");
            Integer circulatingSupply = JsonHelper.getInt(coinData, "circulating_supply");
            Integer totalSupply = JsonHelper.getInt(coinData, "total_supply");
            Boolean isActive = JsonHelper.getBoolean(JsonHelper.getInt(coinData, "is_active"));
            Boolean isFiduciary = JsonHelper.getBoolean(JsonHelper.getInt(coinData, "is_fiat"));
            Integer ranking = JsonHelper.getInt(coinData, "cmc_rank");

            Coin coin = new Coin(name, symbol, maxSupply, circulatingSupply, totalSupply, isActive, isFiduciary, ranking);
            coins.add(coin);
        }
        return coins;
    }
}


