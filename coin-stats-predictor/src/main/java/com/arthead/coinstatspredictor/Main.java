package com.arthead.coinstatspredictor;

import com.arthead.coinstatspredictor.infrastructure.adapters.historicaleventprocessor.HistoricalEventProcessingCoordinator;
import com.arthead.coinstatspredictor.infrastructure.ports.HistoricalEventProcessor;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <path_datamart>");
            return;
        }
        String datamartPath = args[0];
        HistoricalEventProcessor processor = new HistoricalEventProcessingCoordinator(datamartPath);
        processor.processEventStore();
    }
}

