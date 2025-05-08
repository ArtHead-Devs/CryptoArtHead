package application.adapters.coinprovider;

import com.arthead.application.adapters.coinprovider.CoinMarketCapConnection;
import com.arthead.application.adapters.coinprovider.CoinMarketCapDeserializer;
import com.arthead.application.adapters.coinprovider.CoinMarketCapFetcher;
import com.arthead.application.adapters.coinprovider.CoinMarketCapProvider;
import com.arthead.domain.CoinMarketCapData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CoinMarketCapProviderTest {
    private CoinMarketCapConnection connection;
    private CoinMarketCapFetcher fetcher;
    private CoinMarketCapDeserializer deserializer;

    @Before
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
        CoinMarketCapData coins = provider.provide();
        Assert.assertNotNull(coins);
        Assert.assertFalse(coins.getCoins().isEmpty());
    }

}
