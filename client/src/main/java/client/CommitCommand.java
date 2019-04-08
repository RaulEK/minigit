package client;

import java.io.IOException;

public class CommitCommand extends Command {

    public CommitCommand() {}

    @Override
    public void process(String[] input) throws IOException {
        setInput(input);
        if (getInput().length == 3) {
            String message = getInput()[2];
            String commitName = getInput()[1];
            try {
                ClientService.commitRepository(commitName, message, System.getProperty("user.dir"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid parameters. Try: commit commitHash commitName");
        }
    }
}