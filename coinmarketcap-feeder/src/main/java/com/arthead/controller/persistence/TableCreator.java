package com.arthead.controller.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreator {
    public static void createTables(SQLiteConnection connection) {
        String createCoinsTable = """
                CREATE TABLE IF NOT EXISTS coins (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    symbol TEXT NOT NULL,
                    name TEXT NOT NULL UNIQUE,
                    max_supply INTEGER,
                    circulating_supply INTEGER,
                    total_supply INTEGER,
                    is_active BOOLEAN,
                    is_fiduciary BOOLEAN,
                    ranking INTEGER,
                    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
                    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (coin_name) REFERENCES coins (name)
                );
                """;

        try (Connection conn = connection.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(createCoinsTable);
             PreparedStatement stmt2 = conn.prepareStatement(createQuotesTable)) {
            stmt1.execute();
            stmt2.execute();
        } catch (SQLException e) {
            System.err.println("Error creando tablas: " + e.getMessage());
        }
    }
}
