package application.adapters.coinprovider;

import com.arthead.domain.Coin;
import com.arthead.application.adapters.coinprovider.CoinMarketCapDeserializer;
import com.arthead.domain.CoinMarketCapData;
import com.arthead.domain.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CoinMarketCapDeserializerTest {

    private String json;

    @Before
    public void setUp() {
        json = "{\n" +
                "  \"data\": {\n" +
                "    \"1\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Bitcoin\",\n" +
                "      \"symbol\": \"BTC\",\n" +
                "      \"slug\": \"bitcoin\",\n" +
                "      \"is_active\": 1,\n" +
                "      \"is_fiat\": 0,\n" +
                "      \"circulating_supply\": 17199862,\n" +
                "      \"total_supply\": 17199862,\n" +
                "      \"max_supply\": 21000000,\n" +
                "      \"quote\": {\n" +
                "        \"USD\": {\n" +
                "          \"price\": 6602.60701122,\n" +
                "          \"volume_24h\": 4314444687.5194,\n" +
                "          \"market_cap\": 852164659250.2758\n" +
                "        },\n" +
                "        \"EUR\": {\n" +
                "          \"price\": 6200.75,\n" +
                "          \"volume_24h\": 4000000000.0,\n" +
                "          \"market_cap\": 800000000000.0\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"1027\": {\n" +
                "      \"id\": 1027,\n" +
                "      \"name\": \"Ethereum\",\n" +
                "      \"symbol\": \"ETH\",\n" +
                "      \"circulating_supply\": 100000000,\n" +
                "      \"total_supply\": 110000000,\n" +
                "      \"quote\": {\n" +
                "        \"USD\": {\n" +
                "          \"price\": 400.50,\n" +
                "          \"volume_24h\": 2000000000.0,\n" +
                "          \"market_cap\": 50000000000.0\n" +
                "        },\n" +
                "        \"EUR\": {\n" +
                "          \"price\": 380.20,\n" +
                "          \"volume_24h\": 1900000000.0,\n" +
                "          \"market_cap\": 48000000000.0\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    @Test
    public void deserializerTest() {
        CoinMarketCapDeserializer deserializer = new CoinMarketCapDeserializer();
        CoinMarketCapData data = deserializer.deserialize(json);

        List<Coin> coins = data.getCoins();
        Assert.assertEquals(2, coins.size());

        Coin bitcoin = coins.getFirst();
        Assert.assertEquals("Bitcoin", bitcoin.getName());
        Assert.assertEquals("BTC", bitcoin.getSymbol());
        Assert.assertEquals(Integer.valueOf(21000000), bitcoin.getMaxSupply());
        Assert.assertEquals(Integer.valueOf(17199862), bitcoin.getCirculatingSupply());
        Assert.assertTrue(bitcoin.getActive());

        Coin ethereum = coins.get(1);
        Assert.assertEquals("Ethereum", ethereum.getName());
        Assert.assertEquals("ETH", ethereum.getSymbol());
        Assert.assertNull(ethereum.getMaxSupply());
        Assert.assertEquals(Integer.valueOf(100000000), ethereum.getCirculatingSupply());

        List<Quote> quotes = data.getQuotes();
        Assert.assertEquals(4, quotes.size());

        Quote bitcoinUsd = quotes.getFirst();
        Assert.assertEquals("Bitcoin", bitcoinUsd.getCoin());
        Assert.assertEquals("USD", bitcoinUsd.getCurrency());
        Assert.assertEquals(852164659250.2758, bitcoinUsd.getMarketCap(), 0.001);
        Assert.assertEquals(6602.60701122, bitcoinUsd.getPrice(), 0.001);
        Assert.assertEquals(4314444687.5194, bitcoinUsd.getVolumeIn24h(), 0.001);

        Quote bitcoinEur = quotes.get(1);
        Assert.assertEquals("Bitcoin", bitcoinEur.getCoin());
        Assert.assertEquals("EUR", bitcoinEur.getCurrency());
        Assert.assertEquals(800000000000.0, bitcoinEur.getMarketCap(), 0.001);
        Assert.assertEquals(6200.75, bitcoinEur.getPrice(), 0.001);
        Assert.assertEquals(4000000000.0, bitcoinEur.getVolumeIn24h(), 0.001);

        Quote ethereumUsd = quotes.get(2);
        Assert.assertEquals("Ethereum", ethereumUsd.getCoin());
        Assert.assertEquals("USD", ethereumUsd.getCurrency());
        Assert.assertEquals(50000000000.0, ethereumUsd.getMarketCap(), 0.001);
        Assert.assertEquals(400.50, ethereumUsd.getPrice(), 0.001);
        Assert.assertEquals(2000000000.0, ethereumUsd.getVolumeIn24h(), 0.001);

        Quote ethereumEur = quotes.get(3);
        Assert.assertEquals("Ethereum", ethereumEur.getCoin());
        Assert.assertEquals("EUR", ethereumEur.getCurrency());
        Assert.assertEquals(48000000000.0, ethereumEur.getMarketCap(), 0.001);
        Assert.assertEquals(380.20, ethereumEur.getPrice(), 0.001);
        Assert.assertEquals(1900000000.0, ethereumEur.getVolumeIn24h(), 0.001);
    }

}
