package com.arthead.githubfeeder.domain;

public class GithubResponse {
    private final Information information;
    private final Repository repository;

    public GithubResponse(Information information, Repository repository) {
        this.information = information;
        this.repository = repository;
    }

    public Information getInformation() {
        return information;
    }

    public Repository getRepository() {
        return repository;
    }
}
