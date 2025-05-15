package com.arthead.githubfeeder.infrastructure.adapters.store.SQLite;

import com.arthead.githubfeeder.domain.Information;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InformationRepository {
    private final SQLiteConnection dbManager;

    public InformationRepository(SQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }

    public void insertInformation(Information information) {
        try (Connection connection = dbManager.getConnection()) {
            if (!informationExists(connection, information)) {
                insertNewInformation(connection, information);
            }
        } catch (SQLException e) {
            System.err.println("Error al procesar moneda. " + e);
        }
    }

    private boolean informationExists(Connection connection, Information information) throws SQLException {
        String checkCoinSQL = """
            SELECT 1 FROM information
            WHERE name = ? AND stars IS ? AND forks IS ? AND issues IS ? AND watchers IS ? AND ss = ?
            """;

        try (PreparedStatement checkStatement = connection.prepareStatement(checkCoinSQL)) {
            setCheckParameters(checkStatement, information);
            try (ResultSet result = checkStatement.executeQuery()) {
                return result.next();
            }
        }
    }

    private void setCheckParameters(PreparedStatement statement, Information information) throws SQLException {
        statement.setString(1, information.getName());
        statement.setInt(2, information.getStars());
        statement.setInt(3, information.getForks());
        statement.setInt(4, information.getIssuesAndPullRequest());
        statement.setInt(5, information.getWatchers());
        statement.setString(6, information.getSs());
    }

    private void insertNewInformation(Connection connection, Information information) throws SQLException {
        String insertInfoSQL = """
            INSERT INTO information (name, stars, forks, issues, watchers, ss, processed_at) VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

        try (PreparedStatement insertStatement = connection.prepareStatement(insertInfoSQL)) {
            setInsertParameters(insertStatement, information);
            insertStatement.executeUpdate();
        }
    }

    private void setInsertParameters(PreparedStatement statement, Information information) throws SQLException {
        statement.setString(1, information.getName());
        statement.setInt(2, information.getStars());
        statement.setInt(3, information.getForks());
        statement.setInt(4, information.getIssuesAndPullRequest());
        statement.setInt(5, information.getWatchers());
        statement.setString(6, information.getSs());
        statement.setString(7, information.getTs());
    }
}