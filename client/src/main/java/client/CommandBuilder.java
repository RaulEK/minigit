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

        availableCommands.put("push", new PushCommand());
        availableCommands.put("commit", new CommitCommand());
        availableCommands.put("pull", new PullCommand());
        availableCommands.put("init", new InitCommand());
        availableCommands.put("log", new LogCommand());
        availableCommands.put("checkout", new CheckoutCommand());
    }

    public Command findCommandByName() throws IOException {
        if (availableCommands.get(command) != null) {
            return availableCommands.get(command);
        }
        return null;
    }
}