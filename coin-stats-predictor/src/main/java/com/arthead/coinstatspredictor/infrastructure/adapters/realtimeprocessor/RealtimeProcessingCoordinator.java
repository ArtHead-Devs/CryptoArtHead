package com.arthead.coinstatspredictor.infrastructure.adapters.realtimeprocessor;

import com.arthead.coinstatspredictor.infrastructure.adapters.common.CoinRepositoryAssociator;
import com.arthead.coinstatspredictor.infrastructure.ports.CsvParser;
import com.arthead.coinstatspredictor.infrastructure.ports.RealTimeEventProcessor;
import com.google.gson.JsonObject;
import jakarta.jms.JMSException;

import java.io.IOException;
import java.util.List;

public class RealtimeProcessingCoordinator implements RealTimeEventProcessor {
    private final CsvCoordinator csvCoordinator;
    private final CsvFileHandler cryptoCsvHandler;
    private final CsvFileHandler githubCsvHandler;
    private final CsvParser cryptoParser;
    private final CsvParser githubParser;
    private final CoinRepositoryAssociator datamartMerger;
    private final DatamartWriter datamartWriter;

    public RealtimeProcessingCoordinator(CsvCoordinator csvCoordinator, MessageReceiver messageReceiver,
                                         CsvFileHandler cryptoCsvHandler, CsvFileHandler githubCsvHandler,
                                         CsvParser cryptoParser, CsvParser githubParser,
                                         CoinRepositoryAssociator datamartMerger, DatamartWriter datamartWriter) throws JMSException {
        this.csvCoordinator = csvCoordinator;
        this.cryptoCsvHandler = cryptoCsvHandler;
        this.githubCsvHandler = githubCsvHandler;
        this.cryptoParser = cryptoParser;
        this.githubParser = githubParser;
        this.datamartMerger = datamartMerger;
        this.datamartWriter = datamartWriter;
        messageReceiver.start();
    }

    @Override
    public void processRealtimeData() {
        try {
            List<String> cryptoLines = cryptoCsvHandler.readNewLines();
            List<String> githubLines = githubCsvHandler.readNewLines();

            if (cryptoLines.isEmpty() && githubLines.isEmpty()) {
                return;
            }

            List<JsonObject> cryptoData = cryptoParser.parse(cryptoLines);
            List<JsonObject> githubData = githubParser.parse(githubLines);

            cryptoCsvHandler.updateLineCount(cryptoLines.size());
            githubCsvHandler.updateLineCount(githubLines.size());

            List<JsonObject> mergedData = datamartMerger.associateCoinsWithRepositories(cryptoData, githubData);
            if (!mergedData.isEmpty()) {
                datamartWriter.writeDatamart(mergedData);
                System.out.println("Updated datamart with " + mergedData.size() + " new entries");
            }
        } catch (IOException e) {
            System.err.println("Error processing CSV data: " + e.getMessage());
        }
    }
}
