package server;

import models.MessageIds;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.UUID;

public class Server {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        try (ServerSocket ss = new ServerSocket(7543)){

            System.out.println("Starting server on port 7543");
            while (true) {

                socket = ss.accept();

                Thread thread = new Thread(new ServerCode(socket));
                thread.start();
            }
        }
    }
}

class ServerCode implements Runnable {

    private final Socket socket;

    public ServerCode(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Client connected.");

        try(socket;
            InputStream is = this.socket.getInputStream();
            DataInputStream inputStream = new DataInputStream(is)) {

            /* Reads message type */
            int type = inputStream.readInt();

            String temporaryArchiveName = UUID.randomUUID().toString();

            String fs = File.separator;

            String serverResourcesPath = Paths.get("..","..", "src", "main", "resources").toString();

            System.out.println(serverResourcesPath);

            if (type == MessageIds.PUSH_RECEIVED) {

                /* Reads all bytes from client */
                byte[] bytes = is.readAllBytes();

                /* Creates a ZIP file from the bytes the client has sent to the server/main/resources folder. */
                FileUtils.writeByteArrayToFile(new File(serverResourcesPath + fs + temporaryArchiveName + ".zip"), bytes);

                /* Extracts the zip file to the server/main/resources folder. */
                ZipFile zip = new ZipFile(serverResourcesPath + fs + temporaryArchiveName + ".zip");
                zip.extractAll(serverResourcesPath);

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
