package com.arthead.application.ports;

import com.arthead.domain.GithubData;

import java.util.Map;

public interface RepositoryProvider {
    GithubData provide(Map<String, String> repoQuery);
}
