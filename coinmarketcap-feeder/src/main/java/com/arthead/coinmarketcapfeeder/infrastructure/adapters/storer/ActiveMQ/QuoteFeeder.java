package com.arthead.coinmarketcapfeeder.infrastructure.adapters.storer.ActiveMQ;

import com.arthead.coinmarketcapfeeder.domain.Quote;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.List;

public class QuoteFeeder {
    private final String url;
    private static final String subject = "crypto.Quotes";
    private final Gson gson = new Gson();

    public QuoteFeeder(String url) {
        this.url = url;
    }

    public void sendQuotes(List<Quote> quotes) throws JMSException {
        Connection connection = createAndStartConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = setupTopicProducer(session);
        sendMessages(quotes, producer, session);
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

    private void sendMessages(List<Quote> quotes, MessageProducer producer, Session session) throws JMSException {
        for (Quote quote : quotes) {
            TextMessage message = createMessageForQuote(quote, session);
            producer.send(message);
        }
    }

    private TextMessage createMessageForQuote(Quote quote, Session session) throws JMSException {
        String json = gson.toJson(quote);
        return session.createTextMessage(json);
    }

    private void closeResources(Connection connection) throws JMSException {
        connection.close();
    }
}