package client;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class CommandBuilder {


    // static map of currently available commands in our minigit
    private HashMap<String, Command> availableCommands = new HashMap<>();
    private String command;

    public CommandBuilder(String[] args) {
        this.command = args[0];

        availableCommands.put("push", new PushCommand(Arrays.copyOfRange(args, 1, args.length)));
        availableCommands.put("commit", new CommitCommand(Arrays.copyOfRange(args, 1, args.length)));
    }

    public Command findCommandByName() throws IOException {
        if (availableCommands.get(command) != null) {
            return availableCommands.get(command);
        }
        return null;
    }
}