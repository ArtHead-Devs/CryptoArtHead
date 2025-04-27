package com.arthead;

import com.arthead.controller.consume.CoinMarketCapProvider;
import com.arthead.controller.Controller;
import com.arthead.controller.consume.*;
import com.arthead.controller.persistence.SQLiteStore;
import com.arthead.controller.consume.CoinProvider;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Uso -> java Main <API_KEY> <DB_PATH>");
            return;
        }

        Map<String, String> queries = new HashMap<>();
        queries.put("slug", "bitcoin,dogecoin");
        queries.put("convert", "USD");

        CoinMarketCapConnection connection = new CoinMarketCapConnection(
                args[0],
                "https://pro-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest",
                queries
        );

        CoinProvider provider = new CoinMarketCapProvider(
                connection,
                new CoinMarketCapFetcher(),
                new CoinMarketCapDeserializer()
        );

        SQLiteStore store = new SQLiteStore(args[1]);
        Controller controller = new Controller(provider, store);

        System.out.println("=== Sistema de Actualización Cripto ===");
        System.out.println("Monedas monitorizadas:");
        String[] slugs = queries.get("slug").split(",");
        for (String slug : slugs) {
            System.out.println(" - " + slug);
        }
        System.out.println("Base de datos: " + args[1]);
        System.out.println("Intervalo actualizaciones: 5 minutos");

        controller.execute();
    }
}
