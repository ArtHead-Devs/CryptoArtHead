package com.arthead.application.usecases.collectandstore;

import com.arthead.application.ports.CoinProvider;
import com.arthead.application.ports.CoinStore;
import com.arthead.domain.Coin;
import com.arthead.domain.CoinMarketCapData;
import com.arthead.domain.Quote;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final CoinProvider provider;
    private final CoinStore store;
    private final ScheduledExecutorService scheduler;
    private final DateTimeFormatter formatHora;

    public Controller(CoinProvider provider, CoinStore store) {
        this.provider = provider;
        this.store = store;
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.formatHora = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    public void execute() {
        Runnable task = this::processData;
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
        System.out.println("Controlador iniciado. Datos se actualizarán cada 5 minutos.");
    }

    private void processData() {
        System.out.println("\n[" + LocalTime.now().format(formatHora) + "] Iniciando ciclo de actualización");

        CoinMarketCapData data = provider.provide();

        if (data == null || data.getCoins().isEmpty()) {
            System.out.println("No se recibieron datos de la API");
            return;
        }

        List<Coin> coins = data.getCoins();
        List<Quote> quotes = data.getQuotes();

        store.save(coins, quotes);

        System.out.println("Datos obtenidos (" + coins.size() + " monedas):");
        for (Coin coin : coins) {
            for (Quote quote : quotes) {
                if (quote.getCoin().equals(coin.getName())) {
                    System.out.println("- " + coin.getName() + " (" + quote.getCurrency() + ") | Actualizada");
                }
            }
        }
        System.out.println("Próxima actualización en 5 minutos");
    }
}
