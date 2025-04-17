package com.arthead.persistence;

import com.arthead.model.Repository;

public class SQLiteGithubStore implements GithubRepositoryStore {
    private final GithubRepository githubRepo;
    private final InformationRepository informationRepo;

    public SQLiteGithubStore(String dbPath) {
        GithubSQLiteConnection dbManager = new GithubSQLiteConnection(dbPath);
        dbManager.initializeDatabase();
        this.githubRepo = new GithubRepository(dbManager);
        this.informationRepo = new InformationRepository(dbManager);
    }

    @Override
    public void save(Repository repository) {
        githubRepo.insertRepository(repository);
        informationRepo.insertInformation(repository);
    }
}

