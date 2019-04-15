package client;

import java.io.IOException;

public class CommitCommand extends Command {

    public CommitCommand() {}

    @Override
    public void process() throws IOException {
        if (getInput().length == 1) {
            String message = getInput()[0];
            try {
                ClientService.commitRepository(message, System.getProperty("user.dir"));
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid parameters. Try commit commitName");
        }
    }
}