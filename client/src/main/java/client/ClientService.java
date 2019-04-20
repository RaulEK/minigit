package client;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import models.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


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

            ZipUtils.createZipFileFromFolder(temporaryArchiveName + ".zip", ClientUtils.seekMinigitFolder().toString());

            byte[] bytes = Files.readAllBytes(Paths.get(temporaryArchiveName + ".zip"));

            /* Deletes temporary zip file. */
            Files.delete(Paths.get(temporaryArchiveName + ".zip"));

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
            Repository repository = ClientUtils.readRepository();
            List<Commit> commits = repository.getCommits();
            String latestCommitName = commits.get(commits.size() - 1).getHash() + ".zip";

            Path commitPath = Paths.get(ClientUtils.seekMinigitFolder().toString(), latestCommitName);

            /* Deletes temporary zip file. */
            new File(temporaryArchiveName + ".zip").delete();

            ClientUtils.cleanWorkingDirectory();

            ZipUtils.extractZipFile(commitPath.toString(), ".");
        }
    }

    public static void commitRepository(String message) throws ZipException, IOException {
        // here we should:
        // add the entire working directory to zip file
        // excluding the .minigit folder
        // name the zip some substring of the file's hash and add some pseudorandom value
        // create a commit with the given message and zip file name (without extension)
        // add the commit to the repository
        // save the repository file

        System.out.println("Committing repository");

        String temporaryArchiveName = UUID.randomUUID().toString();

        Path temporaryArchivePath = Paths.get(ClientUtils.seekMinigitFolder().toString(), temporaryArchiveName + ".zip");

        /* Object which is used to create a zip file from working directory. */
        ZipFile zip = new ZipFile(temporaryArchivePath.toString());

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        /* Iterates through all files in the root repository dir and adds them to the zip, if the file isn't .minigit.
         In the future also ignores all files that are in .miniGitIgnore. */
        File dir = new File(ClientUtils.seekRepoRootFolder().toString());
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
        }

        // SHA1 hash of zip file
        System.out.println("Calculating hash and creating commit");
        String hash = ClientUtils.repositoryFileSha1(temporaryArchiveName);

        // Hash we are going to use to identify commit
        // Can't just use the hash of zip because then we can't have same contents and the same timestamp in .zip
        String commitHash = hash.substring(0, 15) + UUID.randomUUID().toString().substring(0,5);

        File newArchiveFile = new File(Paths.get(ClientUtils.seekMinigitFolder().toString(), commitHash + ".zip").toString());
        File temporaryArchiveFile = new File(temporaryArchivePath.toString());
        if(!temporaryArchiveFile.renameTo(newArchiveFile)){
            throw new IOException("Can't rename archive file");
        };

        /* Creates a commit, adds it to a repository and saves the repository. */
        Repository repo = ClientUtils.readRepository();
        List<Commit> commits = repo.getCommits();

        // probably don't need to check if same name commit exists, it's random enough™
        if(commits.isEmpty()) {
            repo.addCommit(new Commit(commitHash, message, ""));
        } else {
            String lastHash = commits.get(commits.size() - 1).getHash();
            repo.addCommit(new Commit(commitHash, message, lastHash));
        }

        ClientUtils.saveRepository(repo);
    }

    public static Repository initRepository(String name) throws IOException {
        Repository repo = new Repository(name);
        ClientUtils.initializeRepoInCurrentFolder(repo);
        System.out.println("Repository \"" + name + "\" initialized." );
        return repo;
    }

    public static void log() throws IOException {
        Repository repo = ClientUtils.readRepository();
        List<Commit> commits = repo.getCommits();

        /* Iterates all commits and prints out commitHash and commitMessage. */
        for (int i = 0; i < commits.size(); i++) {
            System.out.println(i);
            System.out.println("Hash: " + commits.get(i).getHash());
            System.out.println("CommitMessage: " + commits.get(i).getMessage() + "\n");
        }
    }

    public static void checkout(String commitHash) throws ZipException {
        /* Cleans working directory. */
        ClientUtils.cleanWorkingDirectory();

        /* Extracts the required commit to the working directory. */
        ZipUtils.extractZipFile(Paths.get(".minigit", commitHash + ".zip").toString(), ".");
    }

    public static void commitDiffs(String commitHash) throws IOException, ZipException, DiffException {

        /* Temporary directory where files are held while compared. */
        Path tempDir = Paths.get(ClientUtils.seekMinigitFolder().toString(), ".tempCommits");

        /* Creates .tempCommits if it does't exist */
        if(!Files.exists(tempDir)) {
            Files.createDirectory(tempDir);
        }

        /* Commits zip path */
        String commitZipPath = Paths.get(ClientUtils.seekMinigitFolder().toString(), commitHash + ".zip").toString();
        String ancestorHash;
        String commitAncestorZipPath = null;

        String commitTemp;
        String ancestorTemp = null;
        List<String> commitDirPaths;
        List<String> ancestorDirPaths = new ArrayList<>();
        List<String> files;
        List<String> deleted = new ArrayList<>();

        /* Finds commits ancestor hash. */
        if(!(ancestorHash = ClientUtils.getAncestorOfHash(commitHash)).equals("")) {
            commitAncestorZipPath = Paths.get(ClientUtils.seekMinigitFolder().toString(), ancestorHash + ".zip").toString();

            /* Extract two commits into two different folders for comparing. */
            if(!Files.exists(Paths.get(tempDir.toString(), commitHash))) {
                Files.createDirectory(Paths.get(tempDir.toString(), commitHash));
            }
            if (!Files.exists(Paths.get(tempDir.toString(), ancestorHash))) {
                Files.createDirectory(Paths.get(tempDir.toString(), ancestorHash));
            }

            ancestorTemp = Paths.get(tempDir.toString(), ancestorHash).toString();
            ZipUtils.extractZipFile(commitAncestorZipPath, ancestorTemp);
            ancestorDirPaths = new ArrayList<>();
            ClientUtils.getAllFilePathsInDir(new File(ancestorTemp), ancestorDirPaths);

            /* All files that have been removed in commit. */
            deleted = new ArrayList<>();
        }

        commitTemp = Paths.get(tempDir.toString(), commitHash).toString();
        ZipUtils.extractZipFile(commitZipPath, commitTemp);
        commitDirPaths = new ArrayList<>();
        ClientUtils.getAllFilePathsInDir(new File(commitTemp), commitDirPaths);

        /* All files that have been added in commit. */
        List<String> added = new ArrayList<>();

        /* All files in latest commit */
        files = new ArrayList<>();

        /* Gets commit comments. */
        HashMap<String, HashMap<Integer, String>> diffComments = ClientUtils.getCommit(commitHash, ClientUtils.readRepository()).getDiffComments();

        /* Finds all files that have been deleted. */
        for (String ancestorDirPath : ancestorDirPaths) {
            if (!commitDirPaths.contains(ancestorDirPath)) {
                deleted.add(ancestorDirPath);
            }
        }

        /* Finds all files that have been added. */
        for (String commitDirPath : commitDirPaths) {
            if(!ancestorDirPaths.contains(commitDirPath)) {
                added.add(commitDirPath);
            } else {
                files.add(commitDirPath);
            }
        }

        /* Displays all deleted files. */
        for (String path: deleted) {
            System.out.println("__________________________________________________");
            System.out.println("File deleted --- " + Constants.ANSI_RED + path + Constants.ANSI_RESET);
        }

        /* Displays all new files and their content. */
        for (String path: added) {
            System.out.println("__________________________________________________");
            System.out.println("New file --- " + Constants.ANSI_GREEN + path + Constants.ANSI_RESET);
            try {
                List<String> lines = Files.readAllLines(Paths.get(path));
                for (int i = 0; i < lines.size(); i++) {
                    System.out.println((i + 1) + "| " + Constants.ANSI_GREEN + lines.get(i) + Constants.ANSI_RESET);
                    if(diffComments.containsKey(path)) {
                        if(diffComments.get(path).containsKey(i + 1)) {
                            System.out.println("<<< Comment: " + diffComments.get(path).get(i + 1) + " >>>");
                        }
                    }
                }
            } catch (MalformedInputException e) {
                System.out.println(path + " can't read file.");
            }

            System.out.println("__________________________________________________");
        }

        /* Displays all files with changes and their content. */
        for (String path : files) {
            List<String> original;
            List<String> patched;
            try {
                original = Files.readAllLines(new File(ancestorTemp + File.separator + path).toPath());
                patched = Files.readAllLines(new File(commitTemp + File.separator + path).toPath());
            } catch (MalformedInputException e) {
                System.out.println(path + " can't be read.");
                System.out.println("__________________________________________________");
                continue;
            }

            if (DiffUtils.diff(original, patched).getDeltas().isEmpty()) {
                continue;
            }

            System.out.println(path + "\n");

            DiffRowGenerator generator = DiffRowGenerator.create()
                    .showInlineDiffs(true)
                    .ignoreWhiteSpaces(true)
                    .oldTag(f -> "")
                    .newTag(f -> "")
                    .build();
            List<DiffRow> rows = generator.generateDiffRows(original, patched);

            for (int i = 0; i < rows.size(); i++) {
                DiffRow row = rows.get(i);
                if(!row.getNewLine().equals(row.getOldLine())) {
                    if(row.getOldLine() != "") {
                        System.out.println((i + 1) + "| " + Constants.ANSI_RED + row.getOldLine() + Constants.ANSI_RESET);
                    }
                    System.out.println((i + 1) + "| " + Constants.ANSI_GREEN + row.getNewLine() + Constants.ANSI_RESET);
                } else {
                    System.out.println((i + 1) + "| " + row.getNewLine());
                }
                if(diffComments.containsKey(path)) {
                    if(diffComments.get(path).containsKey(i + 1)) {
                        System.out.println("<<< Comment: " + diffComments.get(path).get(i + 1) + " >>>");
                    }
                }
            }
            System.out.println("__________________________________________________");
        }

        /* Commenting on changes. */
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\nComment on changes? (y/n): ");
            String next = scanner.nextLine();
            if(next.equals("y")) {
                System.out.println("File: ");
                String fileName = scanner.nextLine();
                System.out.println("Line: ");
                int line = Integer.parseInt(scanner.nextLine());
                System.out.println("Comment: ");
                String comment = scanner.nextLine();

                Repository repo = ClientUtils.readRepository();

                Commit changedCommit = ClientUtils.getCommit(commitHash, repo);

                changedCommit.addComment(fileName, line, comment);

                ClientUtils.saveRepository(repo);
            }
        }

        /* Deletes temporary directories, where commits were compared. */
        ClientUtils.deleteDirectory(new File(commitTemp));
        if (ancestorTemp != null) {
            ClientUtils.deleteDirectory(new File(ancestorTemp));
        }
    }
}
