package client.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AddAndRemove {

    public static void addFilesToTxt(String files) throws IOException {
        String[] filesArray = files.split(",");
        File txtfile = new File(ClientUtils.seekRepoRootFolder() + "/.minigit/addedfiles.txt");
        for (String file : filesArray) {
            if (new File(ClientUtils.seekRepoRootFolder() + File.separator + file).exists() || file.equals(".")) {
                Files.write(Paths.get(txtfile.getAbsolutePath()), (file + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                System.out.println(file + " does not exist.");
            }
        }
    }
}
