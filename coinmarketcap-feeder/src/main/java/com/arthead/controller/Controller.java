package com.arthead.controller;

import com.arthead.controller.consume.CoinProvider;
import com.arthead.controller.persistence.CoinStore;
import com.arthead.model.Coin;

import java.util.List;

public class Controller {
    private final CoinProvider provider;
    private final CoinStore store;

    public Controller(CoinProvider provider, CoinStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void execute() {
        List<Coin> coins = provider.provide();

        if (coins != null && !coins.isEmpty()) {
            System.out.println("Datos obtenidos (" + coins.size() + " monedas):");
            for (Coin coin : coins) {
                try {
                    store.save(coin);
                    System.out.println("- " + coin.getName() + " | Actualizada");
                } catch (Exception e) {
                    System.out.println("- Error actualizando " + coin.getName() + ": " + e.getMessage());
                }
            }
        } else {
            System.out.println("No se recibieron datos de la API");
        }
    }
}
