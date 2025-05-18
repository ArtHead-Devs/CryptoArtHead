package com.arthead.coinmarketcapfeeder.infrastructure.adapters.storer.SQLite;

import com.arthead.coinmarketcapfeeder.infrastructure.ports.CoinStore;
import com.arthead.coinmarketcapfeeder.domain.Coin;
import com.arthead.coinmarketcapfeeder.domain.Quote;

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
