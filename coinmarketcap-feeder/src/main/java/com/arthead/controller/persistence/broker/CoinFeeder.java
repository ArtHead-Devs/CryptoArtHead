package com.arthead.controller.persistence.broker;

import com.arthead.model.Coin;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;

public class CoinFeeder {
    private final String url;
    private static final String subject = "crypto.Coins";

    public CoinFeeder(String url) {
        this.url = url;
    }

    public void sendCoins(List<Coin> coins) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic(subject);

        MessageProducer producer = session.createProducer(destination);

        Gson gson = new Gson();
        for(Coin coin: coins){
            String json = gson.toJson(coin);
            TextMessage message = session
                    .createTextMessage(json);
            producer.send(message);
            System.out.println("crypto.Coin printing@@ '" + message.getText() + "'");
        }
        connection.close();
    }
}