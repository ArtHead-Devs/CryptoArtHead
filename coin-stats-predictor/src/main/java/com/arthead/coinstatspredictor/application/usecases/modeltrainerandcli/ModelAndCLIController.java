package com.arthead.coinstatspredictor.application.usecases.modeltrainerandcli;

import com.arthead.coinstatspredictor.infrastructure.adapters.userinterface.CLI;
import com.arthead.coinstatspredictor.infrastructure.adapters.userinterface.PythonReader;
import com.arthead.coinstatspredictor.infrastructure.ports.ExternalModelRunner;
import com.arthead.coinstatspredictor.infrastructure.ports.UserInterface;
import com.arthead.coinstatspredictor.util.FileCleanupUtil;

import java.util.List;
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
        launchUserInterface();
    }

    private void trainModel() {
        try {
            FileCleanupUtil.deleteFiles(List.of(csvModelResultPath));
            ExternalModelRunner pythonReader = new PythonReader(csvModelResultPath, datamartPath);
            pythonReader.runModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void launchUserInterface() {
        UserInterface cli = new CLI(csvModelResultPath);
        cli.run();
        shutdown();
    }

    public void shutdown() {
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
