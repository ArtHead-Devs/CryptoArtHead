package com.arthead.controller.persistence;

import com.arthead.model.Repository;

public interface GithubRepositoryStore {
    void save(Repository repository);
}