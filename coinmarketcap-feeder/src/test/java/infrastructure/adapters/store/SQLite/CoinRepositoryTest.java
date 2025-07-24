package infrastructure.adapters.store.SQLite;

import com.arthead.coinmarketcapfeeder.domain.Coin;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.storer.SQLite.CoinRepository;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.storer.SQLite.SQLiteConnection;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.storer.SQLite.TableCreator;
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
import java.sql.SQLException;
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
        Coin coin = new Coin("Bitcoin", "BTC", 21000000L, 19000000L,20000000L,
                true, false, 1, Instant.now().toString());
        coins.add(coin);

        try (Connection dBconnection = connection.getConnection()) {
            coinRepository.insertCoins(coins);

            try (PreparedStatement preparedStatement = dBconnection.prepareStatement("SELECT * FROM coins WHERE name = 'Bitcoin'")) {

                try (ResultSet result = preparedStatement.executeQuery()) {
                    Assert.assertTrue(result.next());
                    Assert.assertEquals("BTC", result.getString("symbol"));
                    Assert.assertEquals("Bitcoin", result.getString("name"));
                    Assert.assertEquals(21000000L, result.getInt("max_supply"));
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
