package com.arthead.coinmarketcapfeeder.application.usecases.collectorandstorer;

import com.arthead.coinmarketcapfeeder.domain.Coin;
import com.arthead.coinmarketcapfeeder.domain.CoinMarketCapResponse;
import com.arthead.coinmarketcapfeeder.domain.Quote;
import com.arthead.coinmarketcapfeeder.infrastructure.ports.CoinProvider;
import com.arthead.coinmarketcapfeeder.infrastructure.ports.CoinStore;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final CoinProvider provider;
    private final CoinStore store;
    private final ScheduledExecutorService scheduler;

    public Controller(CoinProvider provider, CoinStore store) {
        this.provider = provider;
        this.store = store;
        this.scheduler = Executors.newScheduledThreadPool(3);
    }

    public void execute() {
        Runnable task = this::processData;
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
        System.out.println("Controller started. Data will be updated every 5 minutes.");
    }

    private void processData() {
        System.out.println("\nStarting update cycle");

        CoinMarketCapResponse data = provider.provide();

        if (data == null || data.getCoins().isEmpty()) {
            System.out.println("No data received from API");
            return;
        }

        List<Coin> coins = data.getCoins();
        List<Quote> quotes = data.getQuotes();

        store.save(coins, quotes);

        printControllerInformation(coins, quotes);
    }

    private void printControllerInformation(List<Coin> coins, List<Quote> quotes) {
        System.out.println("Data obtained (" + coins.size() + " coins):");
        for (Coin coin : coins) {
            for (Quote quote : quotes) {
                if (quote.getCoin().equals(coin.getName())) {
                    System.out.println("- " + coin.getName() + " (" + quote.getCurrency() + ") | Updated");
                }
            }
        }
        System.out.println("Next update in 5 minutes");
    }
}
