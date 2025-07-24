package com.arthead.githubfeeder.infrastructure.adapters.storer.ActiveMQ;

import com.arthead.githubfeeder.domain.Information;
import com.arthead.githubfeeder.domain.Repository;
import com.arthead.githubfeeder.infrastructure.ports.GithubRepositoryStore;
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
            throw new RuntimeException("Error sending data to ActiveMQ", e);
        }
    }
}
