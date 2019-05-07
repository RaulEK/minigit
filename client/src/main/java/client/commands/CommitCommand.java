package client.commands;

import client.service.CommitRepository;

import java.util.List;

public class CommitCommand implements Command {

    private List<String> filesToAdd;

    public CommitCommand() {}

    public void setFilesToAdd(List<String> filesToAdd) {
        this.filesToAdd = filesToAdd;
    }

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            String message = input[1];
            CommitRepository.commitRepository(message, filesToAdd);
        } else {
            System.out.println("Invalid parameters. Try: commit <commitName>");
        }
    }
}