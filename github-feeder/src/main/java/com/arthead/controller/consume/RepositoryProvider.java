package com.arthead.controller.consume;

import com.arthead.model.GithubData;

import java.util.Map;

public interface RepositoryProvider {
    GithubData provide(Map<String, String> repoQuery);
}
