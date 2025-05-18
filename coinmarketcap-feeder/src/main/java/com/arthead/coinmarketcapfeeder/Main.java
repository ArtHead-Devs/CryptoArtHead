package com.arthead.coinmarketcapfeeder;

import com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider.CoinMarketCapProvider;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider.CoinMarketCapConnection;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider.CoinMarketCapDeserializer;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider.CoinMarketCapFetcher;
import com.arthead.coinmarketcapfeeder.application.usecases.collectorandstorer.Controller;
import com.arthead.coinmarketcapfeeder.infrastructure.ports.CoinStore;
import com.arthead.coinmarketcapfeeder.infrastructure.ports.CoinProvider;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.ActiveMQ.ActiveMQStore;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Usage -> java Main <API_KEY> <DB_PATH/ACTIVEMQ_LINK>");
            return;
        }

        Map<String, String> queries = new HashMap<>();
        queries.put("slug", "ethereum,xrp,tron,cardano,chainlink,avalanche,stellar,polkadot-new,internet-computer,bittensor");
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

        CoinStore store = new ActiveMQStore(args[1]);
        Controller controller = new Controller(provider, store);

        printSystemInformation(queries);

        controller.execute();
    }

    private static void printSystemInformation(Map<String, String> queries) {
        System.out.println("=== Crypto Update System ===");
        System.out.println("Monitored coins:");
        String[] slugs = queries.get("slug").split(",");
        for (String slug : slugs) {
            System.out.println(" - " + slug);
        }
        System.out.println("Update interval: 5 minutes");
    }
}
