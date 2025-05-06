package com.arthead.controller.persistence.SQL;

import com.arthead.controller.persistence.CoinStore;
import com.arthead.model.Coin;
import com.arthead.model.Quote;

import java.util.List;

public class SQLiteStore implements CoinStore {
    private final CoinRepository coinRepo;
    private final QuoteRepository quoteRepo;

    public SQLiteStore(String dbPath) {
        SQLiteConnection dbManager = new SQLiteConnection(dbPath);
        dbManager.initializeDatabase();
        this.coinRepo = new CoinRepository(dbManager);
        this.quoteRepo = new QuoteRepository(dbManager);
    }

    @Override
    public void save(List<Coin> coins, List<Quote> quotes) {
        coinRepo.insertCoins(coins);
        quoteRepo.insertQuotes(quotes);

    }
}
