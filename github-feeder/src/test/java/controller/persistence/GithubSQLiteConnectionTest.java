package controller.persistence;

import com.arthead.controller.persistence.GithubSQLiteConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GithubSQLiteConnectionTest {

    private GithubSQLiteConnection connection;
    private final String databaseName = "dbtest.db";

    @Before
    public void setUp() {
        connection = new GithubSQLiteConnection(databaseName);
        connection.initializeDatabase();
    }

    @Test
    public void testConnectionAndTablesCreated() throws SQLException {
        try (Connection conn = connection.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();

            try (ResultSet rs1 = meta.getTables(null, null, "Repositories", null)) {
                Assert.assertTrue(rs1.next());
            }

            try (ResultSet rs2 = meta.getTables(null, null, "Information", null)) {
                Assert.assertTrue(rs2.next());
            }
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
