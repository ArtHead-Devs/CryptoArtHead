package com.arthead.application.adapters.store.SQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TableCreator {
    public static void createTables(SQLiteConnection connection) {
        String createRepositoryTable = """
                CREATE TABLE IF NOT EXISTS repositories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    owner TEXT NOT NULL,
                    description TEXT,
                    created_at TEXT NOT NULL,
                    updated_at TEXT NOT NULL,
                    pushed_at TEXT NOT NULL,
                    ss TEXT NOT NULL,
                    processed_at TEXT
                );
            """;

        String createInformationTable = """
                CREATE TABLE IF NOT EXISTS information (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    stars INTEGER NOT NULL,
                    forks INTEGER NOT NULL,
                    issues INTEGER NOT NULL,
                    watchers INTEGER NOT NULL,
                    ss TEXT NOT NULL,
                    processed_at TEXT,
                    FOREIGN KEY (name) REFERENCES Repositories(name)
                );
            """;

        try (Connection dbConnection = connection.getConnection();
             PreparedStatement createRepositoryStatement = dbConnection.prepareStatement(createRepositoryTable);
             PreparedStatement createInformationStatement = dbConnection.prepareStatement(createInformationTable)) {

            createRepositoryStatement.execute();
            createInformationStatement.execute();

        } catch (Exception e) {
            System.err.println("Error creando las tablas de la base de datos. " + e);
        }
    }
}

