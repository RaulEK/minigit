package client;

import java.io.IOException;

public class CommitCommand extends Command {

    public CommitCommand() {}

    public void process(String[] input) throws Exception {
        setInput(input);
        if (getInput().length == 2) {
            String message = getInput()[1];
            ClientService.commitRepository(message, System.getProperty("user.dir"));
        } else {
            System.out.println("Invalid parameters. Try commit commitName");
        }
    }
}