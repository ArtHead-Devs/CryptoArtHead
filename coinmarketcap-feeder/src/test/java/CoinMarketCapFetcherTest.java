import com.arthead.controller.consume.CoinMarketCapConnection;
import com.arthead.controller.consume.CoinMarketCapFetcher;
import org.jsoup.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CoinMarketCapFetcherTest {

    @Test
    public void fetcherTest() throws IOException {
        Map<String, String> queries = new HashMap<>();
        queries.put("slug", "bitcoin");
        queries.put("convert", "USD");

        CoinMarketCapConnection coinMarketCapConnection = new CoinMarketCapConnection("b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c",
                "https://sandbox-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest",
                queries);

        Connection httpConnection = coinMarketCapConnection.createConnection();
        CoinMarketCapFetcher responseFetcher = new CoinMarketCapFetcher();
        Assertions.assertTrue(responseFetcher.fetcher(httpConnection).contains("bitcoin"));
        Assertions.assertTrue(responseFetcher.fetcher(httpConnection).contains("USD"));
    }
}
