package controller.persistence;

import com.arthead.controller.persistence.SQLiteConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteConnectionTest {
    private static final String databaseName = "cointest.db";

    @Test
    public void testGetConnection() throws SQLException {
        SQLiteConnection connection = new SQLiteConnection(databaseName);
        try (Connection conn = connection.getConnection()){
            Assertions.assertNotNull(conn);
        }
    }

    @AfterEach
    public void cleanUpDatabase() {
        Path dbPath = Paths.get(databaseName);
        try {
            Files.deleteIfExists(dbPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
