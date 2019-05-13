package client.commands;

import client.service.Init;

public class InitCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 3) {
            Init.initRepository(input[1], input[2]);
        } else {
            System.out.println("Invalid parameters. Try: init <repoName> <host>");
        }
    }
}
