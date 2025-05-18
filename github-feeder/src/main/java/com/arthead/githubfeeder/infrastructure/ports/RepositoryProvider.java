package com.arthead.githubfeeder.infrastructure.ports;

import com.arthead.githubfeeder.domain.GithubResponse;

import java.util.Map;

public interface RepositoryProvider {
    GithubResponse provide(Map<String, String> repoQuery);
}
