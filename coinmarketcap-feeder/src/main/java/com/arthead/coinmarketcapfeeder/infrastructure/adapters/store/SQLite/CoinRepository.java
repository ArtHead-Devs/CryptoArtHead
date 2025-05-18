package com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.SQLite;

import com.arthead.coinmarketcapfeeder.domain.Coin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CoinRepository {
    private final SQLiteConnection dbManager;

    public CoinRepository(SQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }


    public void insertCoins(List<Coin> coins) {
        for(Coin coin:coins){
            try (Connection connection = dbManager.getConnection()) {
                if (!coinExists(connection, coin)) {
                    insertNewCoin(connection, coin);
                }
            } catch (SQLException e) {
                System.err.println("Error processing coin. " + e);
            }
        }
    }

    private boolean coinExists(Connection connection, Coin coin) throws SQLException {
        String checkCoinSQL = """
            SELECT 1 FROM coins
            WHERE symbol = ? AND name = ? AND max_supply IS ? AND circulating_supply IS ? AND total_supply IS ?
              AND is_active IS ? AND is_fiduciary IS ? AND ranking IS ? AND ss = ?
            """;

        try (PreparedStatement checkStatement = connection.prepareStatement(checkCoinSQL)) {
            setCheckParameters(checkStatement, coin);
            try (ResultSet result = checkStatement.executeQuery()) {
                return result.next();
            }
        }
    }

    private void setCheckParameters(PreparedStatement statement, Coin coin) throws SQLException {
        statement.setString(1, coin.getSymbol());
        statement.setString(2, coin.getName());
        statement.setObject(3, coin.getMaxSupply());
        statement.setObject(4, coin.getCirculatingSupply());
        statement.setObject(5, coin.getTotalSupply());
        statement.setObject(6, coin.getActive());
        statement.setObject(7, coin.getFiduciary());
        statement.setObject(8, coin.getRanking());
        statement.setString(9, coin.getSs());
    }

    private void insertNewCoin(Connection connection, Coin coin) throws SQLException {
        String insertCoinSQL = """
            INSERT INTO coins
            (name, symbol, max_supply, circulating_supply, total_supply, is_active, is_fiduciary, ranking, ss,
             processed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

        try (PreparedStatement insertStatement = connection.prepareStatement(insertCoinSQL)) {
            setInsertParameters(insertStatement, coin);
            insertStatement.executeUpdate();
        }
    }

    private void setInsertParameters(PreparedStatement statement, Coin coin) throws SQLException {
        statement.setString(1, coin.getName());
        statement.setString(2, coin.getSymbol());
        statement.setObject(3, coin.getMaxSupply());
        statement.setObject(4, coin.getCirculatingSupply());
        statement.setObject(5, coin.getTotalSupply());
        statement.setObject(6, coin.getActive());
        statement.setObject(7, coin.getFiduciary());
        statement.setObject(8, coin.getRanking());
        statement.setString(9, coin.getSs());
        statement.setString(10, coin.getTs());
    }
}