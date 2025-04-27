package controller.consume;

import com.arthead.controller.consume.CoinMarketCapConnection;
import com.arthead.controller.consume.CoinMarketCapFetcher;
import org.jsoup.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CoinMarketCapFetcherTest {
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
    public void fetcherTest() throws IOException {
        Connection httpConnection = connection.createConnection();
        CoinMarketCapFetcher responseFetcher = new CoinMarketCapFetcher();
        Assert.assertTrue(responseFetcher.fetcher(httpConnection).contains("bitcoin"));
        Assert.assertTrue(responseFetcher.fetcher(httpConnection).contains("USD"));
    }
}
