package application.adapters.store.SQLite;

import com.arthead.application.adapters.store.SQLite.GithubRepository;
import com.arthead.application.adapters.store.SQLite.SQLiteConnection;
import com.arthead.domain.Repository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;

public class GithubRepositoryTest {
    private SQLiteConnection dbConnection;
    private GithubRepository githubRepo;
    private final String testDb = "repositorytest.db";

    @Before
    public void setUp() {
        dbConnection = new SQLiteConnection(testDb);
        dbConnection.initializeDatabase();
        githubRepo = new GithubRepository(dbConnection);
    }

    @Test
    public void testInsertAndRetrieveRepository() throws Exception {
        String timestamp = Instant.now().toString();
        Repository repo = new Repository(
                "test-repo",
                "test-owner",
                "Test repository",
                "2023-01-01T00:00:00Z",
                "2023-01-02T00:00:00Z",
                "2023-01-03T00:00:00Z",
                timestamp
        );

        githubRepo.insertRepository(repo);

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM repositories WHERE name = ? AND owner = ?")) {

            pstmt.setString(1, "test-repo");
            pstmt.setString(2, "test-owner");

            try (ResultSet rs = pstmt.executeQuery()) {
                Assert.assertTrue(rs.next());
                Assert.assertEquals("test-owner", rs.getString("owner"));
                Assert.assertEquals("Test repository", rs.getString("description"));
                Assert.assertEquals(timestamp, rs.getString("processed_at"));
            }
        }
    }

    @After
    public void cleanUp() {
        try {
            Files.deleteIfExists(Paths.get(testDb));
        } catch (Exception e) {
            System.err.println("Error limpiando test: " + e.getMessage());
        }
    }
}
