package com.arthead.application.adapters.store.ActiveMQ;

import com.arthead.application.ports.GithubRepositoryStore;
import com.arthead.domain.Information;
import com.arthead.domain.Repository;
import jakarta.jms.JMSException;

public class ActiveMQStore implements GithubRepositoryStore {
    private final RepositoryFeeder repositoryFeeder;
    private final InformationFeeder informationFeeder;

    public ActiveMQStore(String brokerUrl) {
        this.repositoryFeeder = new RepositoryFeeder(brokerUrl);
        this.informationFeeder = new InformationFeeder(brokerUrl);
    }

    @Override
    public void save(Repository repository, Information information) {
        try {
            repositoryFeeder.sendRepository(repository);
            informationFeeder.sendInformation(information);
        } catch (JMSException e) {
            throw new RuntimeException("Error enviando datos a ActiveMQ", e);
        }
    }
}
