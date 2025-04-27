package com.arthead;

import com.arthead.controller.GithubController;
import com.arthead.controller.consume.GithubConnection;
import com.arthead.controller.consume.GithubDeserializer;
import com.arthead.controller.consume.GithubFetcher;
import com.arthead.controller.consume.GithubProvider;
import com.arthead.controller.persistence.GithubRepositoryStore;
import com.arthead.controller.persistence.SQLiteGithubStore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Uso -> java Main <API_KEY> <DB_PATH>");
            return;
        }

        Map<String, String> queries = new HashMap<>();
        queries.put("owner", "octocat");
        queries.put("repo", "Hello-World");

        System.out.println("=== SISTEMA DE ACTUALIZACIÓN DE REPOSITORIOS ===");
        GithubConnection connection = new GithubConnection(
                args[0],
                "https://api.github.com/repos/" + queries.get("owner") + "/" + queries.get("repo"),
                queries
        );

        GithubProvider provider = new GithubProvider(
                connection,
                new GithubFetcher(),
                new GithubDeserializer()
        );

        GithubRepositoryStore store = new SQLiteGithubStore(args[1]);

        GithubController controller = new GithubController(provider, store);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Ejecutando solicitud...");
            controller.execute();
        }, 0, 1000, TimeUnit.SECONDS);
    }
}
