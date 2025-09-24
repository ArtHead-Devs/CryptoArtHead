package infrastructure.adapters.coinprovider;

import com.arthead.coinmarketcapfeeder.infrastructure.adapters.coinprovider.CoinMarketCapConnection;
import org.jsoup.Connection;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

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
    public void createConnectionTest() {
        Connection httpConnection = connection.createConnection();
        Assert.assertNotNull(httpConnection);
        Assert.assertEquals("https://sandbox-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest",
                httpConnection.request().url().toString());
    }

    @Test
    public void testConnectionResponse() throws IOException {
        Connection.Response response = connection.createConnection().execute();
        Assert.assertEquals(200, response.statusCode());
    }
}
