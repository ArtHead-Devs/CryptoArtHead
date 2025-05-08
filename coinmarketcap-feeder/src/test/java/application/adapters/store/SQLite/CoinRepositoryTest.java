package application.adapters.store.SQLite;

import com.arthead.application.adapters.store.SQLite.CoinRepository;
import com.arthead.application.adapters.store.SQLite.SQLiteConnection;
import com.arthead.application.adapters.store.SQLite.TableCreator;
import com.arthead.domain.Coin;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.IOException;
import java.sql.*;
import org.junit.Assert;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CoinRepositoryTest {
    private SQLiteConnection connection;
    private CoinRepository coinRepository;
    private final String databaseName = "cointest.db";

    @Before
    public void setUp() {
        connection = new SQLiteConnection(databaseName);
        TableCreator.createTables(connection);
        coinRepository = new CoinRepository(connection);
    }


    @Test
    public void testInsertCoins() throws SQLException {
        List<Coin> coins = new ArrayList<>();
        Coin coin = new Coin("Bitcoin", "BTC", 21000000, 19000000,20000000,
                true, false, 1, Instant.now().toString());
        coins.add(coin);

        try (Connection dBconnection = connection.getConnection()) {
            coinRepository.insertCoins(coins);

            try (PreparedStatement preparedStatement = dBconnection.prepareStatement("SELECT * FROM coins WHERE name = 'Bitcoin'")) {

                try (ResultSet result = preparedStatement.executeQuery()) {
                    Assert.assertTrue(result.next());
                    Assert.assertEquals("BTC", result.getString("symbol"));
                    Assert.assertEquals("Bitcoin", result.getString("name"));
                    Assert.assertEquals(21000000, result.getInt("max_supply"));
                }

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
