package com.arthead.controller.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GithubTableCreator {
    public static void createTables(Connection conn) throws SQLException {
        String createRepositoryTable = """
                CREATE TABLE IF NOT EXISTS Repositories (
                    owner TEXT NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT,
                    created_at DATETIME NOT NULL,
                    updated_at DATETIME NOT NULL,
                    pushed_at DATETIME NOT NULL,
                    check_date TIMESTAMP NOT NULL,
                    PRIMARY KEY (owner, name)
                );
            """;

        String createInformationTable = """
                CREATE TABLE IF NOT EXISTS Information (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    owner TEXT NOT NULL,
                    repo_name TEXT NOT NULL,
                    stars INTEGER NOT NULL,
                    forks INTEGER NOT NULL,
                    issues INTEGER NOT NULL,
                    watchers INTEGER NOT NULL,
                    check_date TIMESTAMP NOT NULL,
                    FOREIGN KEY (repo_name, owner) REFERENCES Repositories(name, owner)
                );
            """;

        try (PreparedStatement stmt1 = conn.prepareStatement(createRepositoryTable);
             PreparedStatement stmt2 = conn.prepareStatement(createInformationTable)) {
            stmt1.execute();
            stmt2.execute();
        }
    }
}

