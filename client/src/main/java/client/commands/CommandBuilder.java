package client.commands;

import client.service.CommitDiffs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandBuilder {

    // map of currently available commands in our minigit
    private HashMap<String, Command> availableCommands = new HashMap<>();

    public CommandBuilder() {

        availableCommands.put("push", new PushCommand());
        availableCommands.put("commit", new CommitCommand());
        availableCommands.put("pull", new PullCommand());
        availableCommands.put("init", new InitCommand());
        availableCommands.put("log", new LogCommand());
        availableCommands.put("checkout", new CheckoutCommand());
        availableCommands.put("ignore", new IgnoreCommand());
        availableCommands.put("diff", new CommitDiffsCommand());
        availableCommands.put("comment", new CommentOnCommitCommand());
        availableCommands.put("add", new AddCommand());
        availableCommands.put("remove", new RemoveCommand());
        availableCommands.put("status", new StatusCommand());
    }

    public Command findCommandByName(String[] args) throws IOException {
        String command = args[0];

        if (availableCommands.get(command) != null) {

            if (command.equals("commit")) {
                CommitCommand cc = (CommitCommand) availableCommands.get("commit");
                AddCommand ac = (AddCommand) availableCommands.get("add");
                cc.setFilesToAdd(ac.getFilesToAdd());
                ac.setFilesToAdd(new ArrayList<>());
                StatusCommand sc = (StatusCommand) availableCommands.get("status");
                sc.setFilesToAdd(new ArrayList<>());
            } else if (command.equals("add")) {
                StatusCommand sc = (StatusCommand) availableCommands.get("status");
                AddCommand ac = (AddCommand) availableCommands.get("add");
                sc.setFilesToAdd(ac.getFilesToAdd());
            }

            return availableCommands.get(command);
        }
        return null;
    }
}