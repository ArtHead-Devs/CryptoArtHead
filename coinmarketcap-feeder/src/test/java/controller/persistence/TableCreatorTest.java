package controller.persistence;

import com.arthead.controller.persistence.SQLiteConnection;
import com.arthead.controller.persistence.TableCreator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TableCreatorTest {
    private final String databaseName = "cointest.db";

    @Test
    public void testCreateTables() throws SQLException {
        SQLiteConnection connection = new SQLiteConnection(databaseName);
        TableCreator.createTables(connection);

        try (Connection conn = connection.getConnection();
             ResultSet rs = conn.getMetaData().getTables(null, null, "coins", null)) {
            assertTrue(rs.next());
        }

        try (Connection conn = connection.getConnection();
             ResultSet rs = conn.getMetaData().getTables(null, null, "quotes", null)) {
            assertTrue(rs.next());
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
