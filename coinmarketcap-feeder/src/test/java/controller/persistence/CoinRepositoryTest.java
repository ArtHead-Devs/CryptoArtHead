package controller.persistence;

import com.arthead.controller.persistence.CoinRepository;
import com.arthead.controller.persistence.SQLiteConnection;
import com.arthead.controller.persistence.TableCreator;
import com.arthead.model.Coin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.*;
import org.junit.jupiter.api.Assertions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CoinRepositoryTest {
    private SQLiteConnection connection;
    private CoinRepository coinRepository;
    private final String databaseName = "cointest.db";

    @BeforeEach
    public void setUp() throws SQLException {
        connection = new SQLiteConnection(databaseName);
        TableCreator.createTables(connection);
        coinRepository = new CoinRepository(connection);
    }


    @Test
    public void testInsertCoin() throws SQLException {
        Coin coin = new Coin("Bitcoin", "BTC", 21000000, 19000000, 20000000, true, false, 1, null);

        try (Connection conn = connection.getConnection()) {
            coinRepository.insertCoin(coin);

            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM coins WHERE name = 'Bitcoin'")) {

                try (ResultSet rs = pstmt.executeQuery()) {
                    Assertions.assertTrue(rs.next());
                    Assertions.assertEquals("BTC", rs.getString("symbol"));
                    Assertions.assertEquals("Bitcoin", rs.getString("name"));
                    Assertions.assertEquals(21000000, rs.getInt("max_supply"));
                }

            }
        }
    }

    @AfterEach
    public void cleanUpDatabase() {
        Path dbPath = Paths.get(databaseName);
        try {
            Files.deleteIfExists(dbPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

