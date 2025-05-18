package com.arthead.githubfeeder.application.usecases.collectorandstorer;

import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubProvider;
import com.arthead.githubfeeder.infrastructure.ports.GithubRepositoryStore;
import com.arthead.githubfeeder.domain.GithubData;
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
        Runnable task = this::processAllRepositories;
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
        System.out.println("GitHub Controller started - Updates every 5 minutes");
    }

    private void processAllRepositories() {
        System.out.println("\nStarting update cycle:");
        repoList.forEach(this::processRepository);
        System.out.println("Next update in 5 minutes");
    }

    private void processRepository(Map<String, String> repoQuery) {
        String owner = repoQuery.get("owner");
        String repoName = repoQuery.get("repo");
        String formattedName = owner + "/" + repoName;
        try {
            GithubData data = provider.provide(Map.of("owner", owner, "repo", repoName));
            handleRepositoryData(data, formattedName);
        } catch (Exception e) {
            System.out.println("- " + formattedName + ": Error - " + e.getMessage());
        }
    }

    private void handleRepositoryData(GithubData data, String formattedName) {
        if (data == null) {
            System.out.println("- " + formattedName + ": No data received");
        } else {
            store.save(data.getRepository(), data.getInformation());
            System.out.println("- " + formattedName + ": Successfully updated");
        }
    }
}
