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


