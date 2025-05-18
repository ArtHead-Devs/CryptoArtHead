package com.arthead.coinstatspredictor.application.usecases.modeltrainerandcli;

import com.arthead.coinstatspredictor.infrastructure.adapters.userinterface.CLI;
import com.arthead.coinstatspredictor.infrastructure.adapters.userinterface.PythonReader;
import java.util.concurrent.*;

public class ModelAndCLIController {
    private final String csvModelResultPath;
    private final String datamartPath;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ModelAndCLIController(String csvModelResultPath, String datamartPath) {
        this.csvModelResultPath = csvModelResultPath;
        this.datamartPath = datamartPath;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::trainModel, 0, 1, TimeUnit.MINUTES);
        launchCLI();
    }

    private void trainModel() {
        try {
            new PythonReader(csvModelResultPath, datamartPath).runModel();
            System.out.println("\n[System] Model updated successfully at " + java.time.LocalTime.now());
        } catch (Exception e) {
            System.err.println("\n[Error] Model training failed: " + e.getMessage());
        }
    }

    private void launchCLI() {
        CLI cli = new CLI(csvModelResultPath);
        cli.run();
        shutdown();
    }

    public void shutdown() {
        running = false;
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
