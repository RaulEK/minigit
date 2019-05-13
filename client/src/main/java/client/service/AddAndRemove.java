package client.service;

import models.Constants;
import models.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AddAndRemove {

    public static void addFilesToTxt(String files) throws IOException {
        String[] filesArray = files.split(",");
        File txtfile = new File(Utils.seekRepoRootFolder() + "/.minigit/addedfiles.txt");
        for (String file : filesArray) {
            if (new File(Utils.seekRepoRootFolder() + File.separator + file).exists() || file.equals(".")) {
                Files.write(Paths.get(txtfile.getAbsolutePath()), (file + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                System.out.println("File staged: " + Constants.ANSI_GREEN + file + Constants.ANSI_RESET);
            } else {
                System.out.println(file + " does not exist.");
            }
        }
    }
}
