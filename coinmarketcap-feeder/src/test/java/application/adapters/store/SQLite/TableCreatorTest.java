package application.adapters.store.SQLite;

import com.arthead.application.adapters.store.SQLite.SQLiteConnection;
import com.arthead.application.adapters.store.SQLite.TableCreator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


public class TableCreatorTest {
    private final String databaseName = "cointest.db";

    @Test
    public void testCreateTables() throws SQLException {
        SQLiteConnection connection = new SQLiteConnection(databaseName);
        TableCreator.createTables(connection);

        try (Connection dbConnection = connection.getConnection();
             ResultSet rs = dbConnection.getMetaData().getTables(null, null, "coins", null)) {
            Assert.assertTrue(rs.next());
        }

        try (Connection dbConnection = connection.getConnection();
             ResultSet rs = dbConnection.getMetaData().getTables(null, null, "quotes", null)) {
            Assert.assertTrue(rs.next());
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
