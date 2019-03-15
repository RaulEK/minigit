package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Push {
    public static void main(String[] args) throws IOException {

        String repoZip = args[0]; // Repo zip name

        String fs = System.getProperty("file.separator");

        //Sends Repo zip to server.
        System.out.println("Connecting to server.");
        try (Socket socket = new Socket("localhost", 8080);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            byte[] bytes = Files.readAllBytes(Paths.get("src" + fs + "main" + fs + "java" + fs + "client" + fs + "repo" + fs + repoZip));

            // Message type
            outputStream.writeInt(1);

            // Sends repo name
            outputStream.writeUTF(repoZip);

            // Sends zip bytes
            outputStream.writeInt(bytes.length);
            outputStream.write(bytes);

        }
        System.out.println("Connection finished.");
    }
}
