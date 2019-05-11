package client.service;

import models.*;

import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



public class PushRepository {

    public static void pushRepository(Repository repo) throws IOException, ZipException {

        // here we should:
        // zip the entire .minigit folder
        // send it to the server
        // in the server we should unpack it.
        // and attempt to store it
        // if there are any conflicts (commit zips with same name) then reject

        String temporaryArchiveName = UUID.randomUUID().toString();

        /* Sends .minigit folder to the server */
        try (Socket socket = new Socket("localhost", 7543);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            ZipUtils.createZipFileFromFolder(temporaryArchiveName + ".zip", ClientUtils.seekMinigitFolder().toString());

            byte[] bytes = Files.readAllBytes(Paths.get(temporaryArchiveName + ".zip"));

            Files.delete(Paths.get(temporaryArchiveName + ".zip"));

            dos.writeInt(MessageIds.PUSH_RECEIVED);

            dos.writeInt(bytes.length);

            dos.write(bytes, 0, bytes.length);
        }
    }
}
