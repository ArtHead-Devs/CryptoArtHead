import com.arthead.controller.consume.CoinMarketCapConnection;
import com.arthead.controller.consume.CoinMarketCapDeserializer;
import com.arthead.controller.consume.CoinMarketCapFetcher;
import com.arthead.controller.consume.CoinMarketCapProvider;
import com.arthead.model.Coin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinMarketCapProviderTest {
    private CoinMarketCapConnection connection;
    private CoinMarketCapFetcher fetcher;
    private CoinMarketCapDeserializer deserializer;

    @BeforeEach
    public void setUp(){
        Map<String, String> queries = new HashMap<>();
        queries.put("slug", "bitcoin");
        queries.put("convert", "USD");
        connection = new CoinMarketCapConnection("b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c",
                "https://sandbox-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest",
                queries);

        fetcher = new CoinMarketCapFetcher();
        deserializer = new CoinMarketCapDeserializer();
    }

    @Test
    public void providerTest() {
        CoinMarketCapProvider provider = new CoinMarketCapProvider(connection, fetcher, deserializer);

        List<Coin> coins = provider.provide();

        Assertions.assertNotNull(coins);
        Assertions.assertFalse(coins.isEmpty());
    }

}
