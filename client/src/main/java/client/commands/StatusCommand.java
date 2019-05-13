package client.commands;

import models.Constants;
import models.Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StatusCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 1) {
            if (new File(Utils.seekRepoRootFolder() + "/.minigit/addedfiles.txt").exists()) {
                System.out.println("Files staged for commit: ");
                for (String file : Files.readAllLines(Paths.get(Utils.seekRepoRootFolder() + "/.minigit/addedfiles.txt"))) {
                    System.out.println(Constants.ANSI_GREEN + file + Constants.ANSI_RESET);
                }
                System.out.println();
            } else {
                System.out.println("No files have been added.");
            }
        } else {
            System.out.println("Something went wrong. Use 'status' to check commit status");
        }
    }
}
