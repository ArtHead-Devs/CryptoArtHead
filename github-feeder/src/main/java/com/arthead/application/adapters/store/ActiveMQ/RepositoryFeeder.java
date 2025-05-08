package com.arthead.application.adapters.store.ActiveMQ;

import com.arthead.domain.Repository;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class RepositoryFeeder {
    private final String url;
    private static final String topic = "github.Repositories";

    public RepositoryFeeder(String url) {
        this.url = url;
    }

    public void sendRepository(Repository repository) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topic);
            MessageProducer producer = session.createProducer(destination);

            Gson gson = new Gson();
            String json = gson.toJson(repository);
            TextMessage message = session.createTextMessage(json);

            producer.send(message);
            System.out.println("github.Repositories enviado: " + repository.getName());
        }
    }
}
