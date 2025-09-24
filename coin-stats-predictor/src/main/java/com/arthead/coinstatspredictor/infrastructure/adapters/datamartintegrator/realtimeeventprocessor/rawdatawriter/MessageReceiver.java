package com.arthead.coinstatspredictor.infrastructure.adapters.datamartintegrator.realtimeeventprocessor.rawdatawriter;

import com.google.gson.JsonParser;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.List;

public class MessageReceiver {
    private final List<String> topics;
    private final String url;
    private final EventCsvDistributor coordinator;

    public MessageReceiver(List<String> topics, String url, EventCsvDistributor coordinator) {
        this.topics = topics;
        this.url = url;
        this.coordinator = coordinator;
    }

    public void start() throws JMSException {
        Connection connection = createAndStartConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        setupTopicConsumers(session);
    }

    private Connection createAndStartConnection() throws JMSException {
        Connection connection = new ActiveMQConnectionFactory(url).createConnection();
        connection.setClientID("BussinesUnit");
        connection.start();
        return connection;
    }

    private void setupTopicConsumers(Session session) throws JMSException {
        for (String topicName : topics) {
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topic, topicName + "-sub");
            configureMessageListener(consumer, topicName);
        }
    }

    private void configureMessageListener(MessageConsumer consumer, String topicName) throws JMSException {
        consumer.setMessageListener(message -> handleMessage(message, topicName));
    }

    private void handleMessage(Message message, String topicName){
        try {
            if (message instanceof TextMessage textMessage) {
                coordinator.writeEvent(JsonParser.parseString(textMessage.getText()).getAsJsonObject());
            }
        } catch (JMSException e) {
            System.err.println("Error processing message in " + topicName + ": " + e.getMessage());
        }
    }
}
