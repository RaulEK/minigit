package client;

import com.google.gson.Gson;
import models.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            ZipUtils.createZipFileFromFolder(temporaryArchiveName + ".zip", Constants.MINIGIT_DIRECTORY_NAME);

            byte[] bytes = Files.readAllBytes(Paths.get(temporaryArchiveName + ".zip"));

            dos.writeInt(MessageIds.PUSH_RECEIVED);

            /* Send message length and bytes */
            dos.writeInt(bytes.length);

            /* Sends bytes to the client */
            dos.write(bytes, 0, bytes.length);
        }
        System.out.println("Connection finished.");
    }

    public static void pullRepository() throws IOException, ZipException {

        String temporaryArchiveName = UUID.randomUUID().toString();

        System.out.println("Connecting to server.");
        try (Socket socket = new Socket("localhost", 7543);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeInt(MessageIds.PULL_REQUEST);

            int bytesLen = dis.readInt();

            byte[] bytes = dis.readNBytes(bytesLen);

            FileUtils.writeByteArrayToFile(new File(temporaryArchiveName + ".zip"), bytes);

            /* Extracts the zip file to working directory. */
            ZipUtils.extractZipFile(temporaryArchiveName + ".zip", ".");

            /* Finds last commit and extracts to current working directory.  */
            Repository repository = readRepository();
            List<Commit> commits = repository.getCommits();
            String latestCommitName = commits.get(commits.size() - 1).getHash() + ".zip";

            Path commitPath = Paths.get(Constants.MINIGIT_DIRECTORY_NAME, latestCommitName);

            ZipUtils.extractZipFile(commitPath.toString(), ".");
        }
    }

    public static void commitRepository(String message, String workingDir) throws ZipException, IOException {

        // here we should:
        // add the entire working directory to zip file
        // excluding the .minigit folder
        // name the zip some substring of the file's hash and add some pseudorandom value
        // create a commit with the given message and zip file name (without extension)
        // add the commit to the repository
        // save the repository file

        /* Object which is used to create a zip file from working directory. */
        System.out.println("Committing repository");
        ClientService.createFolder();

        String temporaryArchiveName = UUID.randomUUID().toString();

        Path temporaryArchivePath = Paths.get(Constants.MINIGIT_DIRECTORY_NAME, temporaryArchiveName + ".zip");

        ZipFile zip = new ZipFile(temporaryArchivePath.toString());

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        /* Iterates through all files in the working dir and adds them to the zip, if the file isn't .minigit.
         In the future also ignores all files that are in .miniGitIgnore. */
        File dir = new File(workingDir);
        File[] files = dir.listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".minigit")) {
                    continue;
                } else if(file.isDirectory()) {
                    System.out.println("Added folder" + file);
                    zip.addFolder(file, zipParameters);
                }  else {
                    System.out.println("Added file" + file);
                    zip.addFile(file, zipParameters);
                }
            }
        } else {
            // Means that there are no files in the working directory.
        }

        /* Creates a commit, adds it to a repository and saves the repository. */

        // SHA1 hash of zip file
        System.out.println("Calculating hash and creating commit");
        String hash = repositoryFileSha1(temporaryArchiveName);

        // Hash we are going to use to identify commit
        // Can't just use the hash of zip because then we can't have same contents and the same timestamp in .zip
        String commitHash = hash.substring(0, 15) + UUID.randomUUID().toString().substring(0,5);

        File newArchiveFile = new File(Paths.get(Constants.MINIGIT_DIRECTORY_NAME, commitHash + ".zip").toString());
        File temporaryArchiveFile = new File(temporaryArchivePath.toString());
        if(!temporaryArchiveFile.renameTo(newArchiveFile)){
            throw new IOException("Can't rename archive file");
        };

        Repository repo = readRepository();
        List<Commit> commits = repo.getCommits();

        // probably don't need to check if same name commit exists, it's random enough™
        if(commits.isEmpty()) {
            repo.addCommit(new Commit(commitHash, message, ""));
        } else {
            String lastHash = commits.get(commits.size() - 1).getHash();
            repo.addCommit(new Commit(commitHash, message, lastHash));
        }

        saveRepository(repo);
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
            // TODO: remove this later, add this to a separate init command.
            return initRepository("Testing");
        }
    }

    public static Repository initRepository(String name) {
        Repository repo = new Repository(name);
        return repo;
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
}
