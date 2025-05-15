package com.arthead.githubfeeder.infrastructure.adapters.store.SQLite;

import com.arthead.githubfeeder.domain.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GithubRepository {
    private final SQLiteConnection dbManager;

    public GithubRepository(SQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }

    public void insertRepository(Repository repository) {
        try (Connection connection = dbManager.getConnection()) {
            if (!repositoryExists(connection, repository)) {
                insertNewRepository(connection, repository);
            }
        } catch (SQLException e) {
            System.err.println("Error al procesar repositorio: " + e.getMessage());
        }
    }

    private boolean repositoryExists(Connection connection, Repository repository) throws SQLException {
        String checkRepoSQL = """
            SELECT 1 FROM repositories
            WHERE name = ? AND owner = ? AND description = ? AND created_at = ? AND updated_at = ?
            AND pushed_at = ? AND ss = ?
            """;

        try (PreparedStatement checkStatement = connection.prepareStatement(checkRepoSQL)) {
            setCheckParameters(checkStatement, repository);
            try (ResultSet result = checkStatement.executeQuery()) {
                return result.next();
            }
        }
    }

    private void setCheckParameters(PreparedStatement statement, Repository repository) throws SQLException {
        statement.setString(1, repository.getName());
        statement.setString(2, repository.getOwner());
        statement.setString(3, repository.getDescription());
        statement.setString(4, repository.getCreateDate());
        statement.setString(5, repository.getUpdateDate());
        statement.setString(6, repository.getPushDate());
        statement.setString(7, repository.getSs());
    }

    private void insertNewRepository(Connection connection, Repository repository) throws SQLException {
        String insertRepoSQL = """
            INSERT INTO repositories
            (name, owner, description, created_at, updated_at, pushed_at, ss, processed_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
            """;

        try (PreparedStatement insertStatement = connection.prepareStatement(insertRepoSQL)) {
            setInsertParameters(insertStatement, repository);
            insertStatement.executeUpdate();
        }
    }

    private void setInsertParameters(PreparedStatement statement, Repository repository) throws SQLException {
        statement.setString(1, repository.getName());
        statement.setString(2, repository.getOwner());
        statement.setString(3, repository.getDescription());
        statement.setString(4, repository.getCreateDate());
        statement.setString(5, repository.getUpdateDate());
        statement.setString(6, repository.getPushDate());
        statement.setString(7, repository.getSs());
        statement.setString(8, repository.getTs());
    }
}
