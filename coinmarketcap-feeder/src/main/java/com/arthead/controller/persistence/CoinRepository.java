package com.arthead.controller.persistence;

import com.arthead.model.Coin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CoinRepository {
    private final SQLiteConnection dbManager;

    public CoinRepository(SQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }

    public void insertCoin(Coin coin) {
        String insertCoinSQL = """
            INSERT OR IGNORE INTO coins
            (symbol, name, max_supply, circulating_supply, total_supply, is_active, is_fiduciary, ranking, processed_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertCoinSQL)) {
            pstmt.setString(1, coin.getSymbol());
            pstmt.setString(2, coin.getName());
            pstmt.setObject(3, coin.getMaxSupply());
            pstmt.setObject(4, coin.getCirculatingSupply());
            pstmt.setObject(5, coin.getTotalSupply());
            pstmt.setBoolean(6, coin.getActive() != null && coin.getActive());
            pstmt.setBoolean(7, coin.getFiduciary() != null && coin.getFiduciary());
            pstmt.setInt(8, coin.getRanking());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar moneda: " + e.getMessage());
        }
    }
}
