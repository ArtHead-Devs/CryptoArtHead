package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.historicaleventprocessor;

import com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.common.CoinRepositoryAssociator;
import com.arthead.coinstatspredictor.infrastructure.ports.DatamartWriter;
import com.arthead.coinstatspredictor.infrastructure.ports.HistoricalEventProcessor;
import com.arthead.coinstatspredictor.infrastructure.ports.HistoricalEventLoader;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;

public class HistoricalEventProcessingCoordinator implements HistoricalEventProcessor {
    private final HistoricalEventLoader historicalEventReader;
    private final GithubInformationMerger githubInformationMerger;
    private final CoinQuoteMerger coinQuoteMerger;
    private final CoinRepositoryAssociator githubCoinAssociator;
    private final DatamartWriter exporter;

    public HistoricalEventProcessingCoordinator(HistoricalEventLoader historicalEventReader,
                                                GithubInformationMerger githubInformationMerger,
                                                CoinQuoteMerger coinQuoteMerger, CoinRepositoryAssociator githubCoinAssociator,
                                                DatamartWriter exporter) {
        this.historicalEventReader = historicalEventReader;
        this.githubInformationMerger = githubInformationMerger;
        this.coinQuoteMerger = coinQuoteMerger;
        this.githubCoinAssociator = githubCoinAssociator;
        this.exporter = exporter;
    }

    @Override
    public void processHistoricalEvents() {
        try {
            Map<String, List<JsonObject>> coinEvents = historicalEventReader.loadAndGroupHistoricalEvents("eventstore/crypto.Coins/CoinMarketCap");
            Map<String, List<JsonObject>> quoteEvents = historicalEventReader.loadAndGroupHistoricalEvents("eventstore/crypto.Quotes/CoinMarketCap");
            Map<String, List<JsonObject>> githubInformationEvents = historicalEventReader.loadAndGroupHistoricalEvents("eventstore/github.Information/Github");
            Map<String, List<JsonObject>> githubRepositoryEvents = historicalEventReader.loadAndGroupHistoricalEvents("eventstore/github.Repositories/Github");
            List<JsonObject> mergedGithub = githubInformationMerger.mergeRepositoryWithInformation(githubInformationEvents, githubRepositoryEvents);
            List<JsonObject> mergedCrypto = coinQuoteMerger.mergeCoinWithQuotes(coinEvents, quoteEvents);
            List<JsonObject> finalDataset = githubCoinAssociator.associateCoinsWithRepositories(mergedCrypto, mergedGithub);
            exporter.writeDatamart(finalDataset);
            System.out.println("Processing successfully completed");
        } catch (Exception e) {
            System.err.println("Error in processing: " + e.getMessage());
        }
    }
}
