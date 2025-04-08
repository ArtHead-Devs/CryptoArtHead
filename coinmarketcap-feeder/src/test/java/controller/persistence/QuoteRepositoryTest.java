package controller.persistence;

import com.arthead.controller.persistence.QuoteRepository;
import com.arthead.controller.persistence.SQLiteConnection;
import com.arthead.controller.persistence.TableCreator;
import com.arthead.model.CurrencyInfo;
import com.arthead.model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QuoteRepositoryTest {
    private SQLiteConnection connection;
    private QuoteRepository quoteRepository;
    private final String databaseName = "cointest.db";

    @BeforeEach
    public void setUp() throws SQLException {
        connection = new SQLiteConnection(databaseName);
        TableCreator.createTables(connection);
        quoteRepository = new QuoteRepository(connection);
    }

    @Test
    public void testInsertQuote() throws SQLException {
        Map<String, CurrencyInfo> quotes = new HashMap<>();
        CurrencyInfo bitcoinInformation = new CurrencyInfo(50000.0, 1000000.0, 5.0, 0.5, 1.0, -2.0, 10.0, 20.0, 30000000.0, 5000.0);
        quotes.put("USD", bitcoinInformation);
        Quote quote = new Quote(quotes);

        try (Connection conn = connection.getConnection()) {
            quoteRepository.insertQuote(quote, "Bitcoin");

            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM quotes WHERE coin_name = ? AND currency = ?")) {
                pstmt.setString(1, "Bitcoin");
                pstmt.setString(2, "USD");

                try (ResultSet rs = pstmt.executeQuery()) {
                    Assertions.assertTrue(rs.next());
                    Assertions.assertEquals(50000.0, rs.getDouble("price"));
                    Assertions.assertNotEquals(343232, rs.getDouble("market_cap"));
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
