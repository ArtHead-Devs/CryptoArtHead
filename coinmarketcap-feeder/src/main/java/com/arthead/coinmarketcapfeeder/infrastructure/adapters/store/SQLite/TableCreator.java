package com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.SQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TableCreator {
    public static void createTables(SQLiteConnection connection){
        String createCoinsTable = """
                CREATE TABLE IF NOT EXISTS coins (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    symbol TEXT NOT NULL,
                    name TEXT NOT NULL,
                    max_supply INTEGER,
                    circulating_supply INTEGER,
                    total_supply INTEGER,
                    is_active BOOLEAN,
                    is_fiduciary BOOLEAN,
                    ranking INTEGER,
                    ss TEXT NOT NULL,
                    processed_at TEXT
                );
                """;

        String createQuotesTable = """
                CREATE TABLE IF NOT EXISTS quotes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    coin_name TEXT NOT NULL,
                    currency TEXT NOT NULL,
                    price REAL,
                    volume_24h REAL,
                    volume_change_24h REAL,
                    percent_change_1h REAL,
                    percent_change_24h REAL,
                    percent_change_7d REAL,
                    percent_change_30d REAL,
                    percent_change_60d REAL,
                    percent_change_90d REAL,
                    market_cap REAL,
                    ss TEXT NOT NULL,
                    processed_at TEXT,
                    FOREIGN KEY (coin_name) REFERENCES coins (name)
                );
                """;

        try (Connection dbConnection = connection.getConnection();
             PreparedStatement createCoinsStatement = dbConnection.prepareStatement(createCoinsTable);
             PreparedStatement createQuotesStatement = dbConnection.prepareStatement(createQuotesTable)) {

            createCoinsStatement.execute();
            createQuotesStatement.execute();

        } catch (Exception e) {
            System.err.println("Error creating the database tables. " + e);
        }
    }
}
