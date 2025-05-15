package com.arthead.githubfeeder.infrastructure.ports;

import com.arthead.githubfeeder.domain.Information;
import com.arthead.githubfeeder.domain.Repository;

public interface GithubRepositoryStore {
    void save(Repository repository, Information information);
}