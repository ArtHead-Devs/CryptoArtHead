package com.arthead.controller.persistence;

import com.arthead.model.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InformationRepository {
    private final GithubSQLiteConnection dbManager;

    public InformationRepository(GithubSQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }

    public void insertInformation(Repository repo) {
        if (repo == null || repo.getInformation() == null) {
            System.err.println("Error: Repositorio nulo o sin información. Verifica el token de GitHub o si el repositorio existe.");
            return;
        }

        String checkSQL = """
            SELECT 1 FROM Information
            WHERE owner = ? AND repo_name = ? AND stars = ? AND forks = ? AND issues = ? AND watchers = ?
            """;

        String insertSQL = """
            INSERT INTO Information 
            (owner, repo_name, stars, forks, issues, watchers, check_date)
            VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);
            """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {

            checkStmt.setString(1, repo.getOwner().getLogin());
            checkStmt.setString(2, repo.getName());
            checkStmt.setInt(3, repo.getInformation().getStars());
            checkStmt.setInt(4, repo.getInformation().getForks());
            checkStmt.setInt(5, repo.getInformation().getIssues());
            checkStmt.setInt(6, repo.getInformation().getWatchers());

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                        insertStmt.setString(1, repo.getOwner().getLogin());
                        insertStmt.setString(2, repo.getName());
                        insertStmt.setInt(3, repo.getInformation().getStars());
                        insertStmt.setInt(4, repo.getInformation().getForks());
                        insertStmt.setInt(5, repo.getInformation().getIssues());
                        insertStmt.setInt(6, repo.getInformation().getWatchers());
                        insertStmt.executeUpdate();
                        System.out.printf("Información insertada para '%s/%s'.%n",
                                repo.getOwner().getLogin(), repo.getName());
                    }
                } else {
                    System.out.printf("Información ya existente para '%s/%s'. No se insertó duplicado.%n",
                            repo.getOwner().getLogin(), repo.getName());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar información: " + e.getMessage());
        }
    }
}
