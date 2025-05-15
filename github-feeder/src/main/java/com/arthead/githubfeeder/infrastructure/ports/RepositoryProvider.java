package com.arthead.githubfeeder.infrastructure.ports;

import com.arthead.githubfeeder.domain.GithubData;

import java.util.Map;

public interface RepositoryProvider {
    GithubData provide(Map<String, String> repoQuery);
}
