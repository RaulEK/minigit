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

        String temporaryArchiveName = UUID.randomUUID().toString();

        try (Socket socket = new Socket(Utils.readRepository().getHost(), Utils.readRepository().getPort());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

            String currentBranch = Utils.findCurrentBranchJsonFileName();

            Branch.switchToBranch("master");

            ZipUtils.createZipFileFromFolder(temporaryArchiveName + ".zip", Utils.seekMinigitFolder().toString());

            byte[] bytes = Files.readAllBytes(Paths.get(temporaryArchiveName + ".zip"));

            Files.delete(Paths.get(temporaryArchiveName + ".zip"));

            dos.writeInt(MessageIds.PUSH_RECEIVED);

            dos.writeInt(bytes.length);

            dos.write(bytes, 0, bytes.length);

            Branch.switchToBranch(currentBranch.split("\\.")[0]);
        }
    }
}
