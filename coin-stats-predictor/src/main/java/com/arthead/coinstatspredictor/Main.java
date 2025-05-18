package com.arthead.coinstatspredictor;

import com.arthead.coinstatspredictor.application.usecases.eventhandlerandstorer.DatamartEventController;
import com.arthead.coinstatspredictor.application.usecases.modeltrainerandcli.ModelAndCLIController;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.common.CoinRepositoryAssociator;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.historicaleventprocessor.*;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.RealtimeProcessingCoordinator;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.datamartwriter.CoinMarketCapCsvParser;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.datamartwriter.DatamartWriter;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.datamartwriter.GithubCsvParser;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter.CsvCoordinator;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter.CsvFileHandler;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter.MessageReceiver;
import com.arthead.coinstatspredictor.infrastructure.ports.*;
import jakarta.jms.JMSException;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing process. Please wait...");
        if (args.length < 9) {
            System.err.println("Use: java Main <coinmarketcap_csv> <github_csv> <datamart_csv> <modelresults_csv>" +
                    " <port_activemq> <topic1> <topic2> <topic3> <topic4>");
            System.exit(1);
        }
        try {
            HistoricalEventProcessor historicalProcessor = createHistoricalProcessor(args);
            RealTimeEventProcessor realtimeProcessor = createRealtimeProcessor(args);
            DatamartEventController datamartEventController = new DatamartEventController(historicalProcessor, realtimeProcessor,
                    List.of(args[0], args[1], args[2]));
            datamartEventController.startApplication();

            ModelAndCLIController controller = new ModelAndCLIController(
                    args[3],
                    args[2]
            );
            Runtime.getRuntime().addShutdownHook(new Thread(controller::shutdown));
            controller.start();

        } catch (Exception e) {
            System.err.println("Error initialising the application. " + e.getMessage());
            System.exit(1);
        }
    }

    private static HistoricalEventProcessor createHistoricalProcessor(String[] args) {
        return new HistoricalEventProcessingCoordinator(
                new HistoricalEventReader(),
                new GithubInformationMerger(),
                new CoinQuoteMerger(),
                new CoinRepositoryAssociator(),
                new DatamartCsvWriter(args[2])
        );
    }

    private static RealTimeEventProcessor createRealtimeProcessor(String[] args) throws JMSException {
        CsvCoordinator csvCoordinator = new CsvCoordinator(args[0], args[1]);
        return new RealtimeProcessingCoordinator(
                new MessageReceiver(List.of(args[5], args[6], args[7], args[8]),
                        args[4],csvCoordinator),
                new CsvFileHandler(args[0]),
                new CsvFileHandler(args[1]),
                new CoinMarketCapCsvParser(),
                new GithubCsvParser(),
                new CoinRepositoryAssociator(),
                new DatamartWriter(args[2])
        );
    }
}