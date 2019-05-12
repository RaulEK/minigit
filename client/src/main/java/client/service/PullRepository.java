package client.service;

import models.*;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class PullRepository {
    public static void pullRepository() throws IOException, ZipException {

        String temporaryArchiveName = UUID.randomUUID().toString();

        try (Socket socket = new Socket(Utils.readRepository().getHost(), Utils.readRepository().getPort());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeInt(MessageIds.PULL_REQUEST);

            int bytesLen = dis.readInt();

            byte[] bytes = dis.readNBytes(bytesLen);

            FileUtils.writeByteArrayToFile(new File(temporaryArchiveName + ".zip"), bytes);

            try {
                ZipUtils.extractZipFile(temporaryArchiveName + ".zip", String.valueOf(Utils.seekRepoRootFolder()));

                Repository repository = Utils.readRepository();
                List<Commit> commits = repository.getCommits();
                String latestCommitZip = commits.get(commits.size() - 1).getHash() + ".zip";

                Path commitPath = Paths.get(Utils.seekMinigitFolder().toString(), latestCommitZip);

                Utils.cleanWorkingDirectory();

                ZipUtils.extractZipFile(commitPath.toString(), String.valueOf(Utils.seekRepoRootFolder()));

            } catch(IOException e) {
                Files.delete(Paths.get(temporaryArchiveName + ".zip"));
                throw new RuntimeException(e);
            }
        }
    }
}
