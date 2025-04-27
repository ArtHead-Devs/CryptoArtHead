package com.arthead.controller.consume;

import com.arthead.model.Repository;

import java.util.List;

public interface RepositoryProvider {
    List<Repository> provide();
}
