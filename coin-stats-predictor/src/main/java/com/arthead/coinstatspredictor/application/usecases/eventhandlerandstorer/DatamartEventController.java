package com.arthead.coinstatspredictor.application.usecases.eventhandlerandstorer;

import com.arthead.coinstatspredictor.infrastructure.ports.HistoricalEventProcessor;
import com.arthead.coinstatspredictor.infrastructure.ports.RealTimeEventProcessor;
import com.arthead.coinstatspredictor.util.FileCleanupUtil;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatamartEventController {
    private final HistoricalEventProcessor historicalProcessor;
    private final RealTimeEventProcessor realtimeProcessor;
    private final ScheduledExecutorService scheduler;
    private final List<String> csvPaths;

    public DatamartEventController(HistoricalEventProcessor historicalProcessor,
                                   RealTimeEventProcessor realtimeProcessor,
                                   List<String> csvPaths) {
        this.historicalProcessor = historicalProcessor;
        this.realtimeProcessor = realtimeProcessor;
        this.csvPaths = csvPaths;
        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    public void startApplication() {
        FileCleanupUtil.deleteFiles(csvPaths);
        runHistoricalProcessing();
        startRealTimeProcessing();
    }

    private void runHistoricalProcessing() {
        System.out.println("=== STARTING HISTORICAL PROCESSING ===");
        try {
            historicalProcessor.processHistoricalEvents();
        } catch (Exception e) {
            System.err.println("Error during historical processing: " + e.getMessage());
        }
    }

    private void startRealTimeProcessing() {
        System.out.println("\n=== STARTING REAL-TIME PROCESSING ===");
        scheduler.scheduleAtFixedRate(
                this::processRealTime, 0, 1, TimeUnit.MINUTES);
    }

    private void processRealTime() {
        try {
            realtimeProcessor.processRealtimeData();
        } catch (Exception e) {
            System.err.println("Error during real-time processing: " + e.getMessage());
        }
    }
}
