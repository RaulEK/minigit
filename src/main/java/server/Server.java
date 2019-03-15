package server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        try (ServerSocket ss = new ServerSocket(8080)){

            System.out.println("Starting server on port .8080");
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

            String fs = System.getProperty("file.separator");

            String absoluteFilePath = "src" + fs + "main" + fs + "java" + fs + "server" + fs + "repo" + fs;

            if (type == 1 ) { //  Receive push
                // Reads all sent files

                String name = inputStream.readUTF();
                int length = inputStream.readInt();
                byte[] value = new byte[length];
                inputStream.readFully(value);

                File file = new File(absoluteFilePath + fs + name);

                file.createNewFile();

                Files.write(Paths.get(absoluteFilePath + fs + name), value);

            } else {
                throw new IllegalArgumentException("type " + type);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        System.out.println("Connection finished.");
    }
}