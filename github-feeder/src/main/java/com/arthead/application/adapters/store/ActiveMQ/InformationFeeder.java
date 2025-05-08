package com.arthead.application.adapters.store.ActiveMQ;

import com.arthead.domain.Information;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class InformationFeeder {
    private final String url;
    private static final String topic = "github.Information";

    public InformationFeeder(String url) {
        this.url = url;
    }

    public void sendInformation(Information information) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topic);
            MessageProducer producer = session.createProducer(destination);

            Gson gson = new Gson();
            String json = gson.toJson(information);
            TextMessage message = session.createTextMessage(json);

            producer.send(message);
            System.out.println("github.Information enviado: " + information.getName());
        }
    }
}
