package com.arthead.controller.consume;

import com.arthead.model.Coin;
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

            String name = coinData.get("name").getAsString();
            String symbol = coinData.get("symbol").getAsString();
            int maxSupply = coinData.get("max_supply").getAsInt();
            int circulatingSupply = coinData.get("circulating_supply").getAsInt();
            int totalSupply = coinData.get("total_supply").getAsInt();
            boolean isActive = coinData.get("is_active").getAsInt() == 1;
            boolean isFiduciary = coinData.get("is_fiat").getAsInt() == 1;
            int ranking = coinData.get("cmc_rank").getAsInt();

            Coin coin = new Coin(name, symbol, maxSupply, circulatingSupply, totalSupply, isActive, isFiduciary, ranking);
            coins.add(coin);
        }
        return coins;
    }
}


