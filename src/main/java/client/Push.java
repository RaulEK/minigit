package client;

import com.google.gson.Gson;

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

            Gson gson = new Gson();

            Repository repo = new Repository(repoZip, bytes);

            String json = gson.toJson(repo);

            // Message type
            outputStream.writeInt(1);

            outputStream.writeUTF(json);

        }
        System.out.println("Connection finished.");
    }
}
