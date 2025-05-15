package infrastructure.adapters.store.SQLite;

import com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.SQLite.QuoteRepository;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.SQLite.SQLiteConnection;
import com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.SQLite.TableCreator;
import com.arthead.coinmarketcapfeeder.domain.Quote;
import org.junit.After;
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

import static org.junit.Assert.*;

public class QuoteRepositoryTest {
    private SQLiteConnection connection;
    private QuoteRepository quoteRepository;
    private final String databaseName = "test_quotes.db";

    @Before
    public void setUp(){
        connection = new SQLiteConnection(databaseName);
        TableCreator.createTables(connection);
        quoteRepository = new QuoteRepository(connection);
    }

    @Test
    public void testInsertQuotes() throws SQLException {
        List<Quote> quotes = new ArrayList<>();
        Quote quote = new Quote("Bitcoin", "USD", 50000.0, 1000000.0, 5.0,
                0.5, 1.0, -2.0, 10.0, 20.0,
                30000000.0, 5000.0, Instant.now().toString());
        quotes.add(quote);

        quoteRepository.insertQuotes(quotes);

        try (Connection dBconnection = connection.getConnection();
             PreparedStatement preparedStatement = dBconnection.prepareStatement(
                     "SELECT * FROM quotes WHERE coin_name = ? AND currency = ?")) {

            preparedStatement.setString(1, "Bitcoin");
            preparedStatement.setString(2, "USD");

            try (ResultSet result = preparedStatement.executeQuery()) {
                assertTrue("Debería existir el registro", result.next());

                assertEquals(50000.0, result.getDouble("price"), 0.001);
                assertEquals(1000000.0, result.getDouble("volume_24h"), 0.001);
                assertEquals(5.0, result.getDouble("volume_change_24h"), 0.001);
                assertEquals(0.5, result.getDouble("percent_change_1h"), 0.001);
                assertEquals(-2.0, result.getDouble("percent_change_7d"), 0.001);
                assertEquals(10.0, result.getDouble("percent_change_30d"), 0.001);
                assertEquals(30000000.0, result.getDouble("percent_change_90d"), 0.001);

                assertFalse(result.next());
            }
        }
    }

    @After
    public void cleanUpDatabase() throws IOException {
        Path dbPath = Paths.get(databaseName);
        Files.deleteIfExists(dbPath);
    }
}
