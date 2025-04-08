package com.arthead.controller.persistence;

import com.arthead.model.Quote;
import com.arthead.model.CurrencyInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class QuoteRepository {
    private final SQLiteConnection dbManager;

    public QuoteRepository(SQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }

    public void insertQuote(Quote quote, String coinName) {
        String checkQuoteSQL = """
            SELECT 1 FROM quotes
            WHERE coin_name = ? AND currency = ? AND price = ? AND volume_24h = ? AND volume_change_24h = ?
              AND percent_change_1h = ? AND percent_change_24h = ? AND percent_change_7d = ?
              AND percent_change_30d = ? AND percent_change_60d = ? AND percent_change_90d = ?
              AND market_cap = ?
            """;

        String insertQuoteSQL = """
            INSERT INTO quotes
            (coin_name, currency, price, volume_24h, volume_change_24h, percent_change_1h,
             percent_change_24h, percent_change_7d, percent_change_30d, percent_change_60d,
             percent_change_90d, market_cap, processed_at)
             VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);
            """;

        for (Map.Entry<String, CurrencyInfo> entry : quote.getCurrencies().entrySet()) {
            String currency = entry.getKey();
            CurrencyInfo info = entry.getValue();

            try (Connection conn = dbManager.getConnection();
                 PreparedStatement checkStmt = conn.prepareStatement(checkQuoteSQL)) {

                checkStmt.setString(1, coinName);
                checkStmt.setString(2, currency);
                checkStmt.setObject(3, info.getPrice());
                checkStmt.setObject(4, info.getVolumeIn24h());
                checkStmt.setObject(5, info.getVolumeChange24h());
                checkStmt.setObject(6, info.getPercentChange1h());
                checkStmt.setObject(7, info.getPercentChange24h());
                checkStmt.setObject(8, info.getPercentChange7d());
                checkStmt.setObject(9, info.getPercentChange30d());
                checkStmt.setObject(10, info.getPercentChange60d());
                checkStmt.setObject(11, info.getPercentChange90d());
                checkStmt.setObject(12, info.getMarketCap());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuoteSQL)) {
                            insertStmt.setString(1, coinName);
                            insertStmt.setString(2, currency);
                            insertStmt.setObject(3, info.getPrice());
                            insertStmt.setObject(4, info.getVolumeIn24h());
                            insertStmt.setObject(5, info.getVolumeChange24h());
                            insertStmt.setObject(6, info.getPercentChange1h());
                            insertStmt.setObject(7, info.getPercentChange24h());
                            insertStmt.setObject(8, info.getPercentChange7d());
                            insertStmt.setObject(9, info.getPercentChange30d());
                            insertStmt.setObject(10, info.getPercentChange60d());
                            insertStmt.setObject(11, info.getPercentChange90d());
                            insertStmt.setObject(12, info.getMarketCap());
                            insertStmt.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al insertar cotización: " + e.getMessage());
            }
        }
    }
}
