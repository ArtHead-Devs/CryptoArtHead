package com.arthead.application.ports;

import com.arthead.domain.Information;
import com.arthead.domain.Repository;

public interface GithubRepositoryStore {
    void save(Repository repository, Information information);
}