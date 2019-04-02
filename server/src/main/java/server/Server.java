package server;

import com.google.gson.Gson;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
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
            DataInputStream inputStream = new DataInputStream(this.socket.getInputStream())) {
            int type = inputStream.readInt();

            String temporaryArchiveName = UUID.randomUUID().toString();

            String serverFilePath = Paths.get("src","main", "java", "server").toString();

            if (type == 1) { //  Receive push

                /* Read UTF send by client. */
                String json = inputStream.readUTF();

                Gson gson = new Gson();

                byte[] bytes = null;
                bytes = gson.fromJson(json, byte[].class);

                /* Create a ZIP file from the bytes the client has sent to the server folder. */
                FileUtils.writeByteArrayToFile(new File(serverFilePath + File.separator + temporaryArchiveName + ".zip"), bytes);

                /* Extract the zip file to the server folder. */
                ZipFile zip = new ZipFile(serverFilePath + File.separator + temporaryArchiveName + ".zip");
                zip.extractAll(serverFilePath);

            } else {
                throw new IllegalArgumentException("type " + type);
            }
        } catch (IOException | ZipException e){
            throw new RuntimeException(e);
        }
        System.out.println("Connection finished.");
    }
}
