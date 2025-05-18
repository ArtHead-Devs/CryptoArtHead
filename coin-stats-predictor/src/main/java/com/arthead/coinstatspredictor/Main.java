package com.arthead.coinstatspredictor;

import com.arthead.coinstatspredictor.application.usecases.eventhandlerandstorer.EventController;
import com.arthead.coinstatspredictor.infrastructure.adapters.common.CoinRepositoryAssociator;
import com.arthead.coinstatspredictor.infrastructure.adapters.historicaleventprocessor.*;
import com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor.*;
import com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor.CoinMarketCapCsvParser;
import com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor.rawdatawriter.CsvCoordinator;
import com.arthead.coinstatspredictor.infrastructure.ports.*;
import jakarta.jms.JMSException;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            HistoricalEventProcessor historicalProcessor = createHistoricalProcessor(args);
            RealTimeEventProcessor realtimeProcessor = createRealtimeProcessor(args);
            EventController controller = new EventController(historicalProcessor, realtimeProcessor);
            controller.startApplication();

        } catch (Exception e) {
            System.err.println("Error inicializando la aplicación: " + e.getMessage());
            System.exit(1);
        }
    }

    private static HistoricalEventProcessor createHistoricalProcessor(String[] args) {
        return new HistoricalEventProcessingCoordinator(
                new HistoricalEventReader(),
                new GithubInformationMerger(),
                new CoinQuoteMerger(),
                new CoinRepositoryAssociator(),
                new DatamartCsvExporter(args[0])
        );
    }

    private static RealTimeEventProcessor createRealtimeProcessor(String[] args) throws JMSException {
        return new RealtimeProcessingCoordinator(
                new CsvCoordinator(args[1], args[2]),
                new MessageReceiver(List.of(args).subList(3, args.length), args[3], new CsvCoordinator(args[1], args[2])),
                new CsvFileHandler(args[1]),
                new CsvFileHandler(args[2]),
                new CoinMarketCapCsvParser(),
                new GithubCsvParser(),
                new CoinRepositoryAssociator(),
                new DatamartWriter(args[0])
        );
    }
}