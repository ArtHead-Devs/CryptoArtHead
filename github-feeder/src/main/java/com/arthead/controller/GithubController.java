package com.arthead.controller;

import com.arthead.controller.consume.GithubProvider;
import com.arthead.controller.persistence.GithubRepositoryStore;
import com.arthead.model.Repository;

import java.util.List;

public class GithubController {
    private final GithubProvider provider;
    private final GithubRepositoryStore store;

    public GithubController(GithubProvider provider, GithubRepositoryStore store) {
        this.provider = provider;
        this.store = store;
    }

    public void execute() {
        System.out.println("Ejecutando controller.execute con queries");
        System.out.println("Iniciando llamada a la API...");
        List<Repository> repositories = (List<Repository>) provider.provide();
        System.out.println("Respuesta recibida de la API.");

        if (repositories == null) {
            System.out.println("No se recibieron datos de la API.");
            return;
        }

        System.out.println("Se recibieron repositorios.");
        for (Repository repository : repositories) {
            System.out.println("Procesando repositorio: " + repository.getName() + ", owner: " + repository.getOwner());
            store.save(repository);
            System.out.println("Guardado en la base de datos: " + repository.getName() + " (" + repository.getOwner() + ")");
        }
    }
}
