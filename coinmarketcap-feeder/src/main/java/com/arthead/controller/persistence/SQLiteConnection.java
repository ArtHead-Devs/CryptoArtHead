package com.arthead.controller.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private final String dbUrl;

    public SQLiteConnection(String dbPath) {
        this.dbUrl = "jdbc:sqlite:" + dbPath;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getDbUrl());
    }

    public void initializeDatabase() {
        TableCreator.createTables(this);
    }
}
