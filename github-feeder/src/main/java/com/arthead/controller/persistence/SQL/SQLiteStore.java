package com.arthead.controller.persistence.SQL;

import com.arthead.controller.persistence.GithubRepositoryStore;
import com.arthead.model.Information;
import com.arthead.model.Repository;

public class SQLiteStore implements GithubRepositoryStore {
    private final GithubRepository githubRepo;
    private final InformationRepository informationRepo;

    public SQLiteStore(String dbPath) {
        SQLiteConnection dbManager = new SQLiteConnection(dbPath);
        dbManager.initializeDatabase();
        this.githubRepo = new GithubRepository(dbManager);
        this.informationRepo = new InformationRepository(dbManager);
    }

    @Override
    public void save(Repository repository, Information information) {
        githubRepo.insertRepository(repository);
        informationRepo.insertInformation(information);
    }
}

