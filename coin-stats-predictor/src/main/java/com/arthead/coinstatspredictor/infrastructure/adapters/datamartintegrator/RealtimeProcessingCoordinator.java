package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator;

import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.common.CoinRepositoryAssociator;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.datamartwriter.DatamartWriter;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter.CsvFileHandler;
import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter.MessageReceiver;
import com.arthead.coinstatspredictor.infrastructure.ports.CsvParser;
import com.arthead.coinstatspredictor.infrastructure.ports.RealTimeEventProcessor;
import com.google.gson.JsonObject;
import jakarta.jms.JMSException;

import java.io.IOException;
import java.util.List;

public class RealtimeProcessingCoordinator implements RealTimeEventProcessor {
    private final CsvFileHandler cryptoCsvHandler;
    private final CsvFileHandler githubCsvHandler;
    private final CsvParser cryptoParser;
    private final CsvParser githubParser;
    private final CoinRepositoryAssociator datamartMerger;
    private final DatamartWriter datamartWriter;

    public RealtimeProcessingCoordinator(MessageReceiver messageReceiver,
                                         CsvFileHandler cryptoCsvHandler, CsvFileHandler githubCsvHandler,
                                         CsvParser cryptoParser, CsvParser githubParser,
                                         CoinRepositoryAssociator datamartMerger, DatamartWriter datamartWriter) throws JMSException {
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
            }
        } catch (IOException e) {
            System.err.println("Error processing CSV data: " + e.getMessage());
        }
    }
}
