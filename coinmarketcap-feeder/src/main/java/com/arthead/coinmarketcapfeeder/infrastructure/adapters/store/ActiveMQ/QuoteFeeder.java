package com.arthead.coinmarketcapfeeder.infrastructure.adapters.store.ActiveMQ;

import com.arthead.coinmarketcapfeeder.domain.Quote;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;

public class QuoteFeeder {
    private final String url;
    private static final String subject = "crypto.Quotes";

    public QuoteFeeder(String url) {
        this.url = url;
    }

    public void sendQuotes(List<Quote> quotes) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic(subject);

        MessageProducer producer = session.createProducer(destination);

        Gson gson = new Gson();
        for(Quote quote: quotes){
            String json = gson.toJson(quote);
            TextMessage message = session
                    .createTextMessage(json);
            producer.send(message);
            System.out.println("crypto.Quotes printing@@ '" + message.getText() + "'");
        }
        connection.close();
    }
}