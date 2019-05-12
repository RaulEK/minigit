package server;

import com.google.gson.Gson;
import models.Constants;
import models.Repository;
import models.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ServerUtils {

    public static Repository readRepository() throws IOException {
        File file = new File(String.valueOf(Paths.get(Constants.MINIGIT_DIRECTORY_NAME, Utils.findCurrentBranchJsonFileName())));
        Gson gson = new Gson();

        try {
            Repository repo = gson.fromJson(new FileReader(file, StandardCharsets.UTF_8), Repository.class);
            return repo;
        } catch(IOException e) {
            throw new IOException(e);
        }
    }

    public static String findLastCommitHash() throws IOException {
        Repository repository = readRepository();
        return repository.getCommits().get(repository.getCommits().size() - 1).getHash();
    }

    public static String getProjectName() throws IOException {
        Repository repository = readRepository();
        return repository.getName();
    }

}
