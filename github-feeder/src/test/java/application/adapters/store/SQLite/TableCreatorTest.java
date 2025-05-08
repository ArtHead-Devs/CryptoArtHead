package application.adapters.store.SQLite;

import com.arthead.application.adapters.store.SQLite.SQLiteConnection;
import com.arthead.application.adapters.store.SQLite.TableCreator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableCreatorTest {
    private final String databaseName = "repositorytest.db";

    @Test
    public void testCreateTables() throws SQLException {
        SQLiteConnection connection = new SQLiteConnection(databaseName);
        TableCreator.createTables(connection);

        try (Connection dbConnection = connection.getConnection();
             ResultSet rs1 = dbConnection.getMetaData().getTables(null, null, "repositories", null)) {
            Assert.assertTrue(rs1.next());
        }

        try (Connection dbConnection = connection.getConnection();
             ResultSet rs2 = dbConnection.getMetaData().getTables(null, null, "information", null)) {
            Assert.assertTrue(rs2.next());
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
