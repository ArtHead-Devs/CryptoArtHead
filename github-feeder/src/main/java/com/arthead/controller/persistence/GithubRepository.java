package com.arthead.controller.persistence;

import com.arthead.model.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class GithubRepository {
    private final GithubSQLiteConnection dbManager;

    public GithubRepository(GithubSQLiteConnection dbManager) {
        this.dbManager = dbManager;
    }

    public void insertRepository(Repository repo) {
        String insertRepoSQL = """
            INSERT OR IGNORE INTO Repositories 
            (owner, name, description, created_at, updated_at, pushed_at, check_date)
            VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);
            """;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertRepoSQL)) {

            pstmt.setString(1, repo.getOwner().getLogin());
            pstmt.setString(2, repo.getName());
            pstmt.setString(3, repo.getDescription());
            pstmt.setString(4, dateFormat.format(repo.getCreateDate()));
            pstmt.setString(5, dateFormat.format(repo.getUpdateDate()));
            pstmt.setString(6, dateFormat.format(repo.getPushDate()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar repositorio: " + e.getMessage());
        }
    }
}
