package client;

import models.Repository;

import java.util.Arrays;
import java.util.List;

public class CommandBuilder {


    // static list of currently available commands in our minigit
    private static List<String> availableCommands = Arrays.asList("push", "commit");

    private String[] input; // command written by user (args)
    private String message; // message of commit
    private Repository repository; // repository
    private String command; // specific command from availableCommands list
    private String conclusion; // Feedback if everything worked out or not

    public CommandBuilder(String[] input) throws Exception {
        this.input = input;
        repository = ClientService.readRepository();
    }

    // Checks if inserted commands exist and if the length is correct
    public boolean buildCommand() {
        if (availableCommands.contains(input[0])) {
            command = input[0];
            if (command.equals("push") && input.length == 2) {
                return true;
            } else if (command.equals("commit") && input.length == 3) {
                return true;
            }
        }
        conclusion = "Command is invalid";
        return false;
    }

    // Executes the command by calling appropriate methods from ClientServcie.java
    public void execute() throws Exception {
        if (buildCommand()) {
            if (command.equals("push")) {
                ClientService.pushRepository(repository);
                conclusion = "Push was succesful";
            } else if (command.equals("commit")) {
                message = input[2];
                String commitName = input[1];
                ClientService.commitRepository(commitName, message, System.getProperty("user.dir"));
                conclusion = "Commit was succesful";
            }
        }
        System.out.println(conclusion);
    }
}
/*
    pushRepository(Repository repo)
    commitRepository(String commitName, String message, String workingDir)
*/