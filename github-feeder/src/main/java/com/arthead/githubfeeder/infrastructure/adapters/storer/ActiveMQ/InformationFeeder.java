package com.arthead.githubfeeder.infrastructure.adapters.storer.ActiveMQ;

import com.arthead.githubfeeder.domain.Information;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class InformationFeeder {
    private final String url;
    private static final String subject = "github.Information";
    private final Gson gson = new Gson();

    public InformationFeeder(String url) {
        this.url = url;
    }

    public void sendInformation(Information information) throws JMSException {
        Connection connection = createAndStartConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = setupTopicProducer(session);
        sendMessage(information, producer, session);
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

    private void sendMessage(Information information, MessageProducer producer, Session session) throws JMSException {
        TextMessage message = createMessageForInformation(information, session);
        producer.send(message);
    }

    private TextMessage createMessageForInformation(Information information, Session session) throws JMSException {
        String json = gson.toJson(information);
        return session.createTextMessage(json);
    }

    private void closeResources(Connection connection) throws JMSException {
        connection.close();
    }
}
