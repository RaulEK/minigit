package server;

import com.google.gson.Gson;
import models.Constants;
import models.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

public class ServerUtils {

    public static Repository readRepository() {
        File file = new File(String.valueOf(Paths.get(Constants.MINIGIT_DIRECTORY_NAME, "repository.json")));
        Gson gson = new Gson();

        try {
            Repository repo = gson.fromJson(new FileReader(file, Charset.forName("UTF-8")), Repository.class);
            return repo;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String findLastCommitHash() {
        Repository repository = readRepository();
        return repository.getCommits().get(repository.getCommits().size() - 1).getHash();
    }

    public static String getProjectName() {
        Repository repository = readRepository();
        return repository.getName();
    }

    public static String findAncestorCommitHash() {
        Repository repository = readRepository();
        return repository.getCommits().get(repository.getCommits().size() - 2).getHash();
    }

}
