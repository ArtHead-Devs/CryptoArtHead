package controller.persistence;

import com.arthead.controller.persistence.GithubSQLiteConnection;
import com.arthead.controller.persistence.GithubTableCreator;
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

public class GithubTableCreatorTest {
    private final String databaseName = "tabletest.db";

    @Test
    public void testCreateTables() throws SQLException {
        GithubSQLiteConnection connection = new GithubSQLiteConnection(databaseName);
        Connection conn = connection.getConnection();
        GithubTableCreator.createTables(conn);

        try (ResultSet rs1 = conn.getMetaData().getTables(null, null, "Repositories", null)) {
            Assert.assertTrue(rs1.next());
        }

        try (ResultSet rs2 = conn.getMetaData().getTables(null, null, "Information", null)) {
            Assert.assertTrue(rs2.next());
        }

        conn.close();
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
