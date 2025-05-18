package com.arthead.coinstatspredictor.application.usecases.eventhandlerandstorer;

import com.arthead.coinstatspredictor.infrastructure.ports.HistoricalEventProcessor;
import com.arthead.coinstatspredictor.infrastructure.ports.RealTimeEventProcessor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventController {
    private final HistoricalEventProcessor historicalProcessor;
    private final RealTimeEventProcessor realtimeProcessor;
    private final ScheduledExecutorService scheduler;

    public EventController(HistoricalEventProcessor historicalProcessor,
                          RealTimeEventProcessor realtimeProcessor) {
        this.historicalProcessor = historicalProcessor;
        this.realtimeProcessor = realtimeProcessor;
        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    public void startApplication() {
        runHistoricalProcessing();
        startRealTimeProcessing();
    }

    private void runHistoricalProcessing() {
        System.out.println("=== INICIANDO PROCESAMIENTO HISTÓRICO ===");
        try {
            historicalProcessor.processHistoricalEvents();
        } catch (Exception e) {
            System.err.println("Error en procesamiento histórico: " + e.getMessage());
        }
    }

    private void startRealTimeProcessing() {
        System.out.println("\n=== INICIANDO PROCESAMIENTO EN TIEMPO REAL ===");
        scheduler.scheduleAtFixedRate(
                this::processRealTime,
                0,
                1,
                TimeUnit.MINUTES
        );
    }

    private void processRealTime() {
        try {
            realtimeProcessor.processRealtimeData();
        } catch (Exception e) {
            System.err.println("Error en procesamiento tiempo real: " + e.getMessage());
        }
    }
}
