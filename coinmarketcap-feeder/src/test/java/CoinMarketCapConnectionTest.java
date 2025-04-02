import com.arthead.controller.consume.CoinMarketCapConnection;
import org.jsoup.Connection;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CoinMarketCapConnectionTest {
    private CoinMarketCapConnection connection;

    @Before
    public void setUp(){
        Map<String, String> queries = new HashMap<>();
        queries.put("slug", "bitcoin");
        queries.put("convert", "USD");

        connection = new CoinMarketCapConnection("b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c",
                "https://sandbox-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest",
                queries);
    }

    @Test
    public void createConnectionTest() throws IOException {
        Connection httpConnection = connection.createConnection();
        Assertions.assertNotNull(httpConnection);
        Assertions.assertEquals("https://sandbox-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest",
                httpConnection.request().url().toString());
    }

    @Test
    public void testConnectionResponse() {
        try {
            Connection.Response response = connection.createConnection().execute();
            Assertions.assertEquals(200, response.statusCode());
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
