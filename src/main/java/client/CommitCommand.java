package client;

public class CommitCommand extends Command {

    public CommitCommand(String[] input) {
        super(input);
    }

    @Override
    public void process() {
        if (getInput().length == 2) {
            String message = getInput()[1];
            String commitName = getInput()[0];
            ClientService.commitRepository(commitName, message, System.getProperty("user.dir"));
        }
    }
}
