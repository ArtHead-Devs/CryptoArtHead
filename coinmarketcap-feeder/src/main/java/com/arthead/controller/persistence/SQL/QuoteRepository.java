package com.arthead.controller.persistence.SQL;

import com.arthead.model.Quote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuoteRepository {
    private final SQLiteConnection dbManager;

    public QuoteRepository(SQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }

    public SQLiteConnection getDbManager() {
        return dbManager;
    }

    public void insertQuotes(List<Quote> quotes) {
        for(Quote quote: quotes){
            try (Connection connection = getDbManager().getConnection()) {
                if (!quoteExists(connection, quote)) {
                    insertNewQuote(connection, quote);
                }
            } catch (SQLException e) {
                System.err.println("Error al procesar cotización. " + e);
            }
        }
    }

    private boolean quoteExists(Connection connection, Quote quote) throws SQLException {
        String checkQuoteSQL = """
            SELECT 1 FROM quotes
            WHERE coin_name = ? AND currency = ? AND price IS ? AND volume_24h IS ? AND volume_change_24h IS ?
              AND percent_change_1h IS ? AND percent_change_24h IS ? AND percent_change_7d IS ?
              AND percent_change_30d IS ? AND percent_change_60d IS ? AND percent_change_90d IS ?
              AND market_cap IS ? AND ss = ?
            """;

        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuoteSQL)) {
            setCheckParameters(checkStatement, quote);
            try (ResultSet result = checkStatement.executeQuery()) {
                return result.next();
            }
        }
    }

    private void setCheckParameters(PreparedStatement statement, Quote quote) throws SQLException {
        statement.setString(1, quote.getCoin());
        statement.setString(2, quote.getCurrency());
        statement.setObject(3, quote.getPrice());
        statement.setObject(4, quote.getVolumeIn24h());
        statement.setObject(5, quote.getVolumeChange24h());
        statement.setObject(6, quote.getPercentChange1h());
        statement.setObject(7, quote.getPercentChange24h());
        statement.setObject(8, quote.getPercentChange7d());
        statement.setObject(9, quote.getPercentChange30d());
        statement.setObject(10, quote.getPercentChange60d());
        statement.setObject(11, quote.getPercentChange90d());
        statement.setObject(12, quote.getMarketCap());
        statement.setString(13, quote.getSs());
    }

    private void insertNewQuote(Connection connection, Quote quote) throws SQLException {
        String insertQuoteSQL = """
            INSERT INTO quotes
            (coin_name, currency, price, volume_24h, volume_change_24h, percent_change_1h,
             percent_change_24h, percent_change_7d, percent_change_30d, percent_change_60d,
             percent_change_90d, market_cap, ss, processed_at)
             VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuoteSQL)) {
            setInsertParameters(insertStmt, quote);
            insertStmt.executeUpdate();
        }
    }

    private void setInsertParameters(PreparedStatement statement, Quote quote) throws SQLException {
        statement.setString(1, quote.getCoin());
        statement.setString(2, quote.getCurrency());
        statement.setObject(3, quote.getPrice());
        statement.setObject(4, quote.getVolumeIn24h());
        statement.setObject(5, quote.getVolumeChange24h());
        statement.setObject(6, quote.getPercentChange1h());
        statement.setObject(7, quote.getPercentChange24h());
        statement.setObject(8, quote.getPercentChange7d());
        statement.setObject(9, quote.getPercentChange30d());
        statement.setObject(10, quote.getPercentChange60d());
        statement.setObject(11, quote.getPercentChange90d());
        statement.setObject(12, quote.getMarketCap());
        statement.setString(13, quote.getSs());
        statement.setString(14, quote.getTs());
    }
}
