package com.arthead.controller.persistence;

import com.arthead.model.Coin;
import com.arthead.model.Quote;

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
    public void save(Coin coin, Quote quote) {
        coinRepo.insertCoin(coin);
        quoteRepo.insertQuote(quote);

    }
}
