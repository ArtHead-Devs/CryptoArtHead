package com.arthead;

import com.arthead.controller.Controller;
import com.arthead.controller.consume.*;
import com.arthead.controller.persistence.SQLiteStore;

import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Uso -> java Main <API_KEY> <DB_PATH>");
            return;
        }

        Map<String, String> queries = new HashMap<>();
        queries.put("slug", "bitcoin,ethereum");
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

        DateTimeFormatter formatHora = DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.println("=== Sistema de Actualización Cripto ===");
        System.out.println("Monedas monitorizadas:");
        String[] slugs = queries.get("slug").split(",");
        for (String slug : slugs) {
            System.out.println(" - " + slug);
        }
        System.out.println("Base de datos: " + args[1]);
        System.out.println("Intervalo actualizaciones: 5 minutos");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        Runnable task = () -> {
            System.out.println("\n[" + LocalTime.now().format(formatHora) + "] Iniciando ciclo de actualización");
            controller.execute();
            System.out.println("Próxima actualización en 5 minutos");
        };

        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
    }
}
