package client.service;

import models.Repository;

import java.io.IOException;

public class Init {
    public static Repository initRepository(String name) throws IOException {
        Repository repo = new Repository(name);
        ClientUtils.initializeRepoInCurrentFolder(repo);
        System.out.println("Repository \"" + name + "\" initialized." );
        return repo;
    }
}
