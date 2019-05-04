package client.commands;

import client.service.CommitRepository;

public class CommitCommand implements Command {

    public CommitCommand() {}

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            String message = input[1];
            CommitRepository.commitRepository(message);
        } else {
            System.out.println("Invalid parameters. Try: commit <commitName>");
        }
    }
}