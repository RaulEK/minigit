package client.commands;

import models.Constants;

import java.util.ArrayList;
import java.util.List;

public class StatusCommand implements Command {

    private List<String> filesToAdd = new ArrayList<>();

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 1) {
            System.out.println("Files staged for commit: ");
            for (String file : filesToAdd) {
                System.out.println(Constants.ANSI_GREEN + file + Constants.ANSI_RESET);
            }
            System.out.println();
        } else {
            System.out.println("Something went wrong. Use 'status' to check commit status");
        }
    }

    public void setFilesToAdd(List<String> filesToAdd) {
        this.filesToAdd = filesToAdd;
    }
}
