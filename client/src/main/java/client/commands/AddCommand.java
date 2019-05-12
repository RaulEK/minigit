package client.commands;

import client.service.AddAndRemove;

public class AddCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            if (input[1].equals(".")) {
                AddAndRemove.addFilesToTxt(".");
            } else {
                AddAndRemove.addFilesToTxt(input[1]);
            }
        } else {
            System.out.println("Invalid parameters, try: add <file1;file2>");
        }
    }
}