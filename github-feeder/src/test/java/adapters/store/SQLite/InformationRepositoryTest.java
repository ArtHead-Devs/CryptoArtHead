package adapters.store.SQLite;

import com.arthead.githubfeeder.domain.Information;
import com.arthead.githubfeeder.infrastructure.adapters.storer.SQLite.InformationRepository;
import com.arthead.githubfeeder.infrastructure.adapters.storer.SQLite.SQLiteConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;

public class InformationRepositoryTest {
    private SQLiteConnection connection;
    private InformationRepository informationRepository;
    private final String databaseName = "repositorytest.db";

    @Before
    public void setUp() {
        connection = new SQLiteConnection(databaseName);
        connection.initializeDatabase();
        informationRepository = new InformationRepository(connection);
    }

    @Test
    public void testInsertInformation() throws Exception {
        String timestamp = Instant.now().toString();
        Information info = new Information(
                "example-repo",
                100,
                50,
                10,
                5,
                timestamp
        );


        informationRepository.insertInformation(info);

        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT stars, forks, issues, watchers, ss, processed_at FROM information WHERE name = ?")) {

            pstmt.setString(1, "example-repo");

            try (ResultSet rs = pstmt.executeQuery()) {
                Assert.assertTrue("Debe existir el registro", rs.next());
                Assert.assertEquals(100, rs.getInt("stars"));
                Assert.assertEquals(50, rs.getInt("forks"));
                Assert.assertEquals(10, rs.getInt("issues"));
                Assert.assertEquals(5, rs.getInt("watchers"));
                Assert.assertEquals("Github", rs.getString("ss"));
                Assert.assertEquals(timestamp, rs.getString("processed_at"));
            }
        }
    }

    @After
    public void cleanUpDatabase() {
        Path dbPath = Paths.get(databaseName);
        try {
            Files.deleteIfExists(dbPath);
        } catch (IOException e) {
            System.err.println("Error cleaning test database. " + e.getMessage());
        }
    }
}
