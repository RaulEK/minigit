package client;

import com.google.gson.Gson;
import models.Constants;
import models.Repository;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ClientUtils {

    public static void cleanWorkingDirectory() {
        File dir = new File(".");
        for(File file: dir.listFiles()) {
            if (!file.getName().equals(".minigit")) {
                file.delete();
            }
        }
    }

    public static Repository readRepository() {
        // here we should:
        // read the repository file from .minigit folder
        // will use this when we need access to the repository object
        Path pathToRepoFile = Paths.get(".minigit","repository.json").normalize();

        File file = new File(pathToRepoFile.toAbsolutePath().toString());

        Gson gson = new Gson();

        try {
            Repository repo = gson.fromJson(new FileReader(file, Charset.forName("UTF-8")), Repository.class);
            return repo;
        } catch(IOException e) {
            System.out.println("Repository has not been initialized. Try: init.");
            throw new RuntimeException(e);
        }
    }

    public static void saveRepository(Repository repository) throws IOException {
        // here we should:
        // serialize the repository
        // save the repository file to .minigit folder

        Path pathToRepoFile = Paths.get(".minigit","repository.json").normalize();

        if(!Files.exists(pathToRepoFile)) {
            createFolder();
        }

        File file = new File(pathToRepoFile.toAbsolutePath().toString());
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(repository, writer);
        }
    }

    public static void createFolder() throws IOException {
        Path pathToRepoFolder = Paths.get( Constants.MINIGIT_DIRECTORY_NAME);
        Files.createDirectories(pathToRepoFolder);
    }

    public static String repositoryFileSha1(String uuid) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(Constants.MINIGIT_DIRECTORY_NAME, uuid + ".zip"))) {
            return org.apache.commons.codec.digest.DigestUtils.sha1Hex(is);
        }
    }
}
