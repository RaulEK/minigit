package client.commands;

import client.service.CommitRepository;

public class CommitCommand extends Command {

    public CommitCommand() {}

    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            String message = input[1];
            CommitRepository.commitRepository(message);
        } else {
            System.out.println("Invalid parameters. Try: commit <commitName>");
        }
    }
}