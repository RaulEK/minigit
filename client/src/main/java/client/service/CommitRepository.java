package client.service;

import models.Commit;
import models.Repository;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommitRepository {
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

        ZipFile zip = new ZipFile(temporaryArchivePath.toString());

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        String source = ClientUtils.seekRepoRootFolder().toString();
        List<String> ignoreTheseFiles = new ArrayList<>();

        if (Files.exists(Paths.get(".minigitignore"))) {
            ignoreTheseFiles = Files.readAllLines(Paths.get(".minigitignore"));
        }

        File tempDir = new File(source, ".commitFiles");

        excludeIgnoredFiles(source, tempDir.getAbsolutePath(), ignoreTheseFiles);

        File[] files = tempDir.listFiles();

        ClientUtils.addFilesToZip(zip, zipParameters, files);

        ClientUtils.deleteDirectory(tempDir);

        // SHA1 hash of zip file
        System.out.println("Calculating hash and creating commit");
        String hash = ClientUtils.repositoryFileSha1(temporaryArchiveName);

        // Hash we are going to use to identify commit
        // Can't just use the hash of zip because then we can't have same contents and the same timestamp in .zip
        String commitHash = hash.substring(0, 15) + UUID.randomUUID().toString().substring(0, 5);

        File newArchiveFile = new File(Paths.get(ClientUtils.seekMinigitFolder().toString(), commitHash + ".zip").toString());
        File temporaryArchiveFile = new File(temporaryArchivePath.toString());

        if (!temporaryArchiveFile.renameTo(newArchiveFile)) {
            throw new IOException("Can't rename archive file");
        }

        Repository repo = ClientUtils.readRepository();
        List<Commit> commits = repo.getCommits();

        if (commits.isEmpty()) {
            repo.addCommit(new Commit(commitHash, message, ""));
        } else {
            String lastHash = commits.get(commits.size() - 1).getHash();
            repo.addCommit(new Commit(commitHash, message, lastHash));
        }

        ClientUtils.saveRepository(repo);
    }

    public static void excludeIgnoredFiles(String source, String destination, List<String> ignoreThese) throws IOException {
        File sourceDir = new File(source);
        File[] currentDirFiles = sourceDir.listFiles();

        if (currentDirFiles != null) {
            for (File file : currentDirFiles) {
                if (file.getName().endsWith(".minigit") || file.getName().endsWith(".commitFiles")) {
                    continue;
                } else if (file.isDirectory()) {
                    new File(destination + File.separator + file.getName()).mkdir();
                    excludeIgnoredFiles(file.getAbsolutePath(), destination + File.separator + file.getName(), ignoreThese);
                } else {
                    if (!ignoreThese.contains(file.getAbsolutePath().replace(ClientUtils.seekRepoRootFolder().toString(), ""))) {
                        FileUtils.copyFileToDirectory(file, new File(destination));
                    }
                }
            }
        }
    }
}