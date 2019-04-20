package client.service;

import models.Commit;
import models.MessageIds;
import models.Repository;
import models.ZipUtils;
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

        try (Socket socket = new Socket("localhost", 7543);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            dos.writeInt(MessageIds.PULL_REQUEST);

            int bytesLen = dis.readInt();

            byte[] bytes = dis.readNBytes(bytesLen);

            FileUtils.writeByteArrayToFile(new File(temporaryArchiveName + ".zip"), bytes);

            try {
                ZipUtils.extractZipFile(temporaryArchiveName + ".zip", String.valueOf(ClientUtils.seekRepoRootFolder()));

                Repository repository = ClientUtils.readRepository();
                List<Commit> commits = repository.getCommits();
                String latestCommitZip = commits.get(commits.size() - 1).getHash() + ".zip";

                Path commitPath = Paths.get(ClientUtils.seekMinigitFolder().toString(), latestCommitZip);

                ClientUtils.cleanWorkingDirectory();

                ZipUtils.extractZipFile(commitPath.toString(), String.valueOf(ClientUtils.seekRepoRootFolder()));

            } catch(IOException e) {
                Files.delete(Paths.get(temporaryArchiveName + ".zip"));
                throw new RuntimeException(e);
            }
        }
    }
}
