package com.arthead.eventstorebuilder.application.usecases.eventstorermanager;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.List;

public class MessageReceiver {
    private final List<String> topics;
    private final String url;
    private final EventStore eventStore;

    public MessageReceiver(List<String> topics, String url) {
        this.topics = topics;
        this.url = url;
        this.eventStore = new EventStore();
    }

    public void start() throws JMSException {
        Connection connection = createAndStartConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        setupTopicConsumers(session);
    }

    private Connection createAndStartConnection() throws JMSException {
        Connection connection = new ActiveMQConnectionFactory(url).createConnection();
        connection.setClientID("EventStoreBuilder");
        connection.start();
        return connection;
    }

    private void setupTopicConsumers(Session session) throws JMSException {
        for (String topicName : topics) {
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topic, topicName + "Subscription");
            configureMessageListener(consumer, topicName);
        }
    }

    private void configureMessageListener(MessageConsumer consumer, String topicName) {
        try {
            consumer.setMessageListener(message -> handleMessage(message, topicName));
        } catch (JMSException e) {
            System.err.println("Error configuring listener for " + topicName + ": " + e.getMessage());
        }
    }

    private void handleMessage(Message message, String topicName) {
        try {
            if (message instanceof TextMessage textMessage) {
                eventStore.saveEvent(topicName, textMessage.getText());
                System.out.println("Event received in: " + topicName);
            }
        } catch (JMSException e) {
            System.err.println("Error processing message in " + topicName + ": " + e.getMessage());
        }
    }
}
