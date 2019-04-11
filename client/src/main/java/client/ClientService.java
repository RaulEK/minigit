package client;

import com.google.gson.Gson;
import models.Commit;
import models.Constants;
import models.MessageIds;
import models.Repository;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.UUID;


public class ClientService {

    public static void pushRepository(Repository repo) throws IOException, ZipException {

        // here we should:
        // zip the entire .minigit folder
        // send it to the server
        // in the server we should unpack it.
        // and attempt to store it
        // if there are any conflicts (commit zips with same name) then reject

        String temporaryArchiveName = UUID.randomUUID().toString();

        /* Sends .minigit folder to the server */
        System.out.println("Connecting to server.");
        try (Socket socket = new Socket("localhost", 7543);
             OutputStream os = socket.getOutputStream();
             DataOutputStream dos = new DataOutputStream(os);) {

            /* Object which is used to create a zip file from the .minigit folder. */
            ZipFile zip = new ZipFile(Paths.get(temporaryArchiveName + ".zip").toString());

            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            /* Creates a zip file from the .minigit folder and reads all bytes. */
            zip.createZipFileFromFolder(".minigit", zipParameters, false, 0);

            byte[] bytes = Files.readAllBytes(Paths.get(temporaryArchiveName + ".zip"));

            /* Sends bytes to the client */
            dos.writeInt(MessageIds.PUSH_RECEIVED);

            os.write(bytes, 0, bytes.length);

            os.flush();
        }
        System.out.println("Connection finished.");
    }

    public static void commitRepository(String commitName, String message, String workingDir) throws ZipException, IOException {

        // here we should:
        // add the entire working directory to zip file
        // excluding the .minigit folder
        // name the zip some substring of the file's hash and add some pseudorandom value
        // create a commit with the given message and zip file name (without extension)
        // add the commit to the repository
        // save the repository file

        /* Object which is used to create a zip file from working directory. */
        System.out.println("Committing repository");

        Repository repo = readRepository();

        // checks if commit name already exists
        if (checkPreviousCommits(commitName, repo)) {

            ClientService.createFolder();
            ZipFile zip = new ZipFile(Paths.get(Constants.MINIGIT_DIRECTORY_NAME, commitName + ".zip").toString());

            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        /* Iterates through all files in the working dir and adds them to the zip, if the file isn't .minigit.
         In the future also ignores all files that are in .miniGitIgnore. */
            File dir = new File(workingDir);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".minigit")) {
                        continue;
                    } else if (file.isDirectory()) {
                        System.out.println("Added folder" + file);
                        zip.addFolder(file, zipParameters);
                    } else {
                        System.out.println("Added file" + file);
                        zip.addFile(file, zipParameters);
                    }
                }
            } else {
                // Means that there are no files in the working directory.
            }

            /* Creates a commit, adds it to a repository and saves the repository. */
            Commit commit = new Commit(commitName, message);

            repo.addCommit(commit);
            saveRepository(repo);

        } else {
            System.out.println("Commit name already exists, commitRepository() is canceled");
        }
    }

    public static Repository readRepository() {
        // here we should:
        // read the repository file from .minigit folder
        // will use this when we need access to the repository object
        Path pathToRepoFile = Paths.get(".minigit", "repository.json").normalize();

        File file = new File(pathToRepoFile.toAbsolutePath().toString());

        Gson gson = new Gson();

        try {
            Repository repo = gson.fromJson(new FileReader(file), Repository.class);
            return repo;
        } catch (IOException e) {
            // TODO: remove this later, add this to a separate init command.
            return initRepository("Testing");
        }
    }

    public static Repository initRepository(String name) {
        Repository repo = new Repository(name);
        return repo;
    }

    public static void createFolder() throws IOException {
        Path pathToRepoFolder = Paths.get(Constants.MINIGIT_DIRECTORY_NAME);
        Files.createDirectories(pathToRepoFolder);
    }

    public static void saveRepository(Repository repository) throws IOException {
        // here we should:
        // serialize the repository
        // save the repository file to .minigit folder

        Path pathToRepoFile = Paths.get(".minigit", "repository.json").normalize();

        if (!Files.exists(pathToRepoFile)) {
            createFolder();
        }

        File file = new File(pathToRepoFile.toAbsolutePath().toString());
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(file);
        gson.toJson(repository, writer);
        writer.close();
    }

    public static boolean checkPreviousCommits(String commitName, Repository repo) {
        for (Commit previousCommit : repo.getCommits()) {
            if (commitName.equals(previousCommit.getHash())) {
                return false;
            }
        }
        return true;
    }
}
