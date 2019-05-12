package client.service;

import models.Utils;
import models.Repository;

import java.io.IOException;
import java.net.Proxy;

public class Init {
    public static Repository initRepository(String name, String host) throws IOException {
        Repository repo = new Repository(name, true, host);
        Utils.initializeRepoInCurrentFolder(repo);
        System.out.println("Repository \"" + name + "\" initialized." );
        return repo;
    }
}
