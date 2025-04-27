package controller.persistence;

import com.arthead.controller.persistence.SQLiteGithubStore;
import com.arthead.model.Information;
import com.arthead.model.Owner;
import com.arthead.model.Repository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteGithubStoreTest {

    private SQLiteGithubStore store;
    private static final String databaseName = "storetest.db";
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        store = new SQLiteGithubStore(databaseName);
        connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
    }

    @Test
    public void saveRepository_ShouldPersistDataInTables() throws Exception {
        Repository repo = createTestRepository();
        store.save(repo);
        verifyRepositoryPersisted(repo);
        verifyInformationPersisted(repo);
    }

    private Repository createTestRepository() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createDate = sdf.parse("2023-01-01 10:00:00");
        Date updateDate = sdf.parse("2023-02-01 10:00:00");
        Date pushDate = sdf.parse("2023-03-01 10:00:00");

        return new Repository(
                "test-repo",
                new Owner("testuser"),
                "repositorio de test",
                createDate,
                updateDate,
                pushDate,
                new Information(7, 3, 99, 17)
        );
    }

    private void verifyRepositoryPersisted(Repository repo) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Repositories WHERE owner = ? AND name = ?"
        )) {
            stmt.setString(1, repo.getOwner().getLogin());
            stmt.setString(2, repo.getName());
            ResultSet rs = stmt.executeQuery();

            Assert.assertTrue(rs.next());
            Assert.assertEquals(repo.getDescription(), rs.getString("description"));
        }
    }

    private void verifyInformationPersisted(Repository repo) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Information WHERE owner = ? AND repo_name = ?"
        )) {
            stmt.setString(1, repo.getOwner().getLogin());
            stmt.setString(2, repo.getName());
            ResultSet rs = stmt.executeQuery();

            Assert.assertTrue(rs.next());
            Assert.assertEquals(repo.getInformation().getStars(), rs.getInt("stars"));
            Assert.assertEquals(repo.getInformation().getForks(), rs.getInt("forks"));
        }
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        Path dbPath = Paths.get(databaseName);
        Files.deleteIfExists(dbPath);
    }
}
