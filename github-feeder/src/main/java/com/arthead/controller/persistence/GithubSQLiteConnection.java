package com.arthead.controller.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GithubSQLiteConnection {
    private final String dbPath;

    public GithubSQLiteConnection(String dbPath) {
        this.dbPath = dbPath;
    }

    public String getDbPath() {
        return dbPath;
    }

    public Connection getConnection() throws SQLException {
        String DB_URL = "jdbc:sqlite:" + getDbPath();
        return DriverManager.getConnection(DB_URL);
    }

    public void initializeDatabase() {
        try (Connection conn = getConnection()) {
            GithubTableCreator.createTables(conn);
        } catch (SQLException e) {
            System.err.println("Error inicializando DB: " + e.getMessage());
        }
    }
}