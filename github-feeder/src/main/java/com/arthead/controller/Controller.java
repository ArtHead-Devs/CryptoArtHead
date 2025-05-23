package com.arthead.controller;

import com.arthead.controller.consume.GithubProvider;
import com.arthead.controller.persistence.GithubRepositoryStore;
import com.arthead.model.GithubData;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final GithubProvider provider;
    private final GithubRepositoryStore store;
    private final ScheduledExecutorService scheduler;
    private final List<Map<String, String>> repoList;

    public Controller(GithubProvider provider, GithubRepositoryStore store,
                      List<Map<String, String>> repoList) {
        this.provider = provider;
        this.store = store;
        this.repoList = repoList;
        this.scheduler = Executors.newScheduledThreadPool(3);
    }

    public void execute() {
        Runnable task = () -> {
            processRepositories();
        };

        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
        System.out.println("Controlador GitHub iniciado - Actualizaciones cada 5 minutos");
    }

    private void processRepositories() {
        System.out.println("\nIniciando ciclo de actualización:");

        for (Map<String, String> repoQuery : repoList) {
            String owner = repoQuery.get("owner");
            String repoName = repoQuery.get("repo");

            try {
                processSingleRepository(owner, repoName);
            } catch (Exception e) {
                System.out.println("- Error en " + owner + "/" + repoName + ": " + e.getMessage());
            }
        }

        System.out.println("Proxima actualización en 5 minutos");
    }

    private void processSingleRepository(String owner, String repoName) {
        try {
            GithubData data = provider.provide(Map.of("owner", owner, "repo", repoName));

            if (data == null) {
                System.out.println("- " + owner + "/" + repoName + ": Sin datos");
                return;
            }

            store.save(data.getRepository(), data.getInformation());
            System.out.println("- " + owner + "/" + repoName + " actualizado");

        } catch (Exception e) {
            System.out.println("- Error procesando " + repoName + ": " + e.getMessage());
        }
    }
}
