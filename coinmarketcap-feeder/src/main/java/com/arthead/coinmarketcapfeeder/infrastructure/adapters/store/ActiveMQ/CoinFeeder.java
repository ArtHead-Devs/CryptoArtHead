package com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.ActiveMQ;

import com.arthead.coinmarketcapfeeder.domain.Coin;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;

public class CoinFeeder {
    private final String url;
    private static final String subject = "crypto.Coins";
    private final Gson gson = new Gson();

    public CoinFeeder(String url) {
        this.url = url;
    }

    public void sendCoins(List<Coin> coins) throws JMSException {
        Connection connection = createAndStartConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = setupTopicProducer(session);
        sendMessages(coins, producer, session);
        closeResources(connection);
    }

    private Connection createAndStartConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    private MessageProducer setupTopicProducer(Session session) throws JMSException {
        Destination destination = session.createTopic(subject);
        return session.createProducer(destination);
    }

    private void sendMessages(List<Coin> coins, MessageProducer producer, Session session) throws JMSException {
        for (Coin coin : coins) {
            TextMessage message = createMessageForCoin(coin, session);
            producer.send(message);
        }
    }

    private TextMessage createMessageForCoin(Coin coin, Session session) throws JMSException {
        String json = gson.toJson(coin);
        return session.createTextMessage(json);
    }

    private void closeResources(Connection connection) throws JMSException {
        connection.close();
    }
}