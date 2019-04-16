package server;

import models.Constants;
import models.MessageIds;
import models.ZipUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class ServerCode implements Runnable {

    private final Socket socket;

    public ServerCode(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Client connected.");

        try(socket;
            DataInputStream dis = new DataInputStream(this.socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream())) {

            /* Reads message type */
            int type = dis.readInt();

            String temporaryArchiveName = UUID.randomUUID().toString() + ".zip";

            System.out.println("Wrote file to folder where serer.jar was started.");

            if (type == MessageIds.PUSH_RECEIVED) {

                /* Read message length */
                int bytesLen = dis.readInt();

                /* Reads all bytes from client */
                byte[] bytes = dis.readNBytes(bytesLen);

                /* Creates a ZIP file from the bytes the client has sent to the server/main/resources folder. */
                FileUtils.writeByteArrayToFile(new File(temporaryArchiveName), bytes);

                /* Extracts the zip file */
                ZipUtils.extractZipFile(temporaryArchiveName, ".");

                new File(temporaryArchiveName).delete();

            } else if (type == MessageIds.PULL_REQUEST) {

                ZipUtils.createZipFileFromFolder(temporaryArchiveName, Constants.MINIGIT_DIRECTORY_NAME);

                byte[] bytes = Files.readAllBytes(Paths.get(temporaryArchiveName));

                /* Sends message length */
                dos.writeInt(bytes.length);

                /* Sends file bytes */
                dos.write(bytes);

            } else {
                throw new IllegalArgumentException("type " + type);
            }
        } catch (ZipException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        System.out.println("Connection finished.");
    }
}
