package client;

import models.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandBuilder {


    // static list of currently available commands in our minigit
    private List<String> availableCommands = Arrays.asList("push", "commit");

    private String command;
    private String[] input; // command parameters


    public CommandBuilder(String[] args) {
        this.command = args[0];
        this.input = Arrays.copyOfRange(args, 1, args.length);
    }

    public void findCommandByName() throws IOException {
        if (availableCommands.contains(command)) {
            switch (command) {
                case "push":
                    PushCommand push = new PushCommand(input);
                    push.process();
                    break;
                case "commit":
                    CommitCommand commit = new CommitCommand(input);
                    commit.process();
                    break;
            }
        }
    }
}
/*


    soovitan teha iga commandi jaoks eraldi class ja siin lihtsalt teha
    findCommandByName(command).process(input). valideerimine ja konkreetse commandi loogika saaks siis ilusti ühes kohas olla
    pushRepository(Repository repo)
    commitRepository(String commitName, String message, String workingDir)
*/