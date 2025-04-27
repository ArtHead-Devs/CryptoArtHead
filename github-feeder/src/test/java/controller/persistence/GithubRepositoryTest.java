package controller.persistence;

import com.arthead.controller.persistence.GithubRepository;
import com.arthead.controller.persistence.GithubSQLiteConnection;
import com.arthead.model.Information;
import com.arthead.model.Owner;
import com.arthead.model.Repository;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class GithubRepositoryTest {

    private GithubSQLiteConnection connection;
    private GithubRepository githubRepository;
    private final String databaseName = "repostest.db";

    @Before
    public void setUp() {
        connection = new GithubSQLiteConnection(databaseName);
        connection.initializeDatabase();
        githubRepository = new GithubRepository(connection);
    }

    @Test
    public void testInsertRepository() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createDate = sdf.parse("2023-01-01 10:00:00");
        Date updateDate = sdf.parse("2023-02-01 10:00:00");
        Date pushDate = sdf.parse("2023-03-01 10:00:00");

        Repository repo = new Repository(
                "example-repo",
                new Owner("johndoe"),
                "Repositorio de prueba",
                createDate,
                updateDate,
                pushDate,
                new Information(10, 5, 100, 20)
        );

        githubRepository.insertRepository(repo);

        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Repositories WHERE name = 'example-repo'")) {

            try (ResultSet rs = pstmt.executeQuery()) {
                Assert.assertTrue(rs.next());
                Assert.assertEquals("johndoe", rs.getString("owner"));
                Assert.assertEquals("example-repo", rs.getString("name"));
                Assert.assertEquals("Repositorio de prueba", rs.getString("description"));
            }
        }
    }

    @After
    public void cleanUpDatabase() {
        Path dbPath = Paths.get(databaseName);
        try {
            Files.deleteIfExists(dbPath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar la base de datos de prueba", e);
        }
    }
}
