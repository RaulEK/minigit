package server;

import models.MessageIds;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
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
            DataInputStream dis = new DataInputStream(this.socket.getInputStream())) {

            /* Reads message type */
            int type = dis.readInt();

            String temporaryArchiveName = UUID.randomUUID().toString();

            String fs = File.separator;

            String serverFolder = Paths.get(".").toString();

            System.out.println("Wrote file to folder where serer.jar was started.");

            if (type == MessageIds.PUSH_RECEIVED) {

                /* Read message length */
                int bytesLen = dis.readInt();

                /* Reads all bytes from client */
                byte[] bytes = dis.readNBytes(bytesLen);

                /* Creates a ZIP file from the bytes the client has sent to the server/main/resources folder. */
                FileUtils.writeByteArrayToFile(new File(serverFolder + fs + temporaryArchiveName + ".zip"), bytes);

                /* Extracts the zip file to the server/main/resources folder. */
                ZipFile zip = new ZipFile(serverFolder + fs + temporaryArchiveName + ".zip");
                zip.extractAll(serverFolder);

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
