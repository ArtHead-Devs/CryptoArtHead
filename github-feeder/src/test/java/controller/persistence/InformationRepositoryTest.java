package controller.persistence;

import com.arthead.controller.persistence.GithubSQLiteConnection;
import com.arthead.controller.persistence.InformationRepository;
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

public class InformationRepositoryTest {

    private GithubSQLiteConnection connection;
    private InformationRepository informationRepository;
    private final String databaseName = "infotest.db";

    @Before
    public void setUp() {
        connection = new GithubSQLiteConnection(databaseName);
        connection.initializeDatabase();
        informationRepository = new InformationRepository(connection);
    }

    @Test
    public void testInsertInformation() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createDate = sdf.parse("2023-01-01 10:00:00");
        Date updateDate = sdf.parse("2023-02-01 10:00:00");
        Date pushDate = sdf.parse("2023-03-01 10:00:00");

        Repository repo = new Repository(
                "example-repo",
                new Owner("johndoe"),
                "repo de prueba",
                createDate,
                updateDate,
                pushDate,
                new Information(10, 5, 100, 20)
        );

        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("""
                INSERT INTO Repositories (owner, name, description, created_at, updated_at, pushed_at, check_date)
                VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            """)) {
            pstmt.setString(1, "johndoe");
            pstmt.setString(2, "example-repo");
            pstmt.setString(3, "repo de prueba");
            pstmt.setString(4, sdf.format(createDate));
            pstmt.setString(5, sdf.format(updateDate));
            pstmt.setString(6, sdf.format(pushDate));
            pstmt.executeUpdate();
        }

        informationRepository.insertInformation(repo);

        try (Connection conn = connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("""
                SELECT * FROM Information WHERE owner = ? AND repo_name = ?
            """)) {
            pstmt.setString(1, "johndoe");
            pstmt.setString(2, "example-repo");

            try (ResultSet rs = pstmt.executeQuery()) {
                Assert.assertTrue(rs.next());
                Assert.assertEquals(100, rs.getInt("stars"));
                Assert.assertEquals(10, rs.getInt("forks"));
                Assert.assertEquals(5, rs.getInt("issues"));
                Assert.assertEquals(20, rs.getInt("watchers"));
            }
        }
    }

    @After
    public void cleanUpDatabase() {
        Path dbPath = Paths.get(databaseName);
        try {
            Files.deleteIfExists(dbPath);
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando la base de datos de prueba", e);
        }
    }
}
