package com.arthead.githubfeeder.infrastructure.adapters.storer.ActiveMQ;

import com.arthead.githubfeeder.domain.Repository;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class RepositoryFeeder {
    private final String url;
    private static final String subject = "github.Repositories";
    private final Gson gson = new Gson();

    public RepositoryFeeder(String url) {
        this.url = url;
    }

    public void sendRepository(Repository repository) throws JMSException {
        Connection connection = createAndStartConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = setupTopicProducer(session);
        sendMessage(repository, producer, session);
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

    private void sendMessage(Repository repository, MessageProducer producer, Session session) throws JMSException {
        TextMessage message = createMessageForRepository(repository, session);
        producer.send(message);
    }

    private TextMessage createMessageForRepository(Repository repository, Session session) throws JMSException {
        String json = gson.toJson(repository);
        return session.createTextMessage(json);
    }

    private void closeResources(Connection connection) throws JMSException {
        connection.close();
    }
}
