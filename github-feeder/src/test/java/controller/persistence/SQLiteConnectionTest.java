package controller.persistence;

import com.arthead.controller.persistence.SQL.SQLiteConnection;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteConnectionTest {
    private final String databaseName = "repositorytest.db";

    @Test
    public void testGetConnection() throws SQLException {
        SQLiteConnection connection = new SQLiteConnection(databaseName);
        try (Connection conn = connection.getConnection()){
            Assert.assertNotNull(conn);
        }
    }

    @After
    public void cleanUpDatabase() {
        Path dbPath = Paths.get(databaseName);
        try {
            Files.deleteIfExists(dbPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
