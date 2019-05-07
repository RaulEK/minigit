package client.commands;

import client.service.ClientUtils;

import java.io.File;

public class RemoveCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            File removeFile = new File(input[1]);
            if (removeFile.delete()) {
                System.out.println("File successfully deleted.");
            } else {
                System.out.println("Program was unable to delete the file");
            }
        } else {
            System.out.println("Try: remove <relativePathToFile>");
        }
    }
}
