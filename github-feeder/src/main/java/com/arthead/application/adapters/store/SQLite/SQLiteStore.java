package com.arthead.application.adapters.store.SQLite;

import com.arthead.application.ports.GithubRepositoryStore;
import com.arthead.domain.Information;
import com.arthead.domain.Repository;

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

