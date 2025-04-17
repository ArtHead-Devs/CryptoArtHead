package com.arthead.persistence;

import com.arthead.model.Repository;

public interface GithubRepositoryStore {
    void save(Repository repository);
}