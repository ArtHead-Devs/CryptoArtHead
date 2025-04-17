package com.arthead.controller;

import com.arthead.model.Repository;

import java.util.List;

public interface RepositoryProvider {
    List<Repository> provide();
}
