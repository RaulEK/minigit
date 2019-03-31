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
            try {
                ClientService.commitRepository(commitName, message, System.getProperty("user.dir"));
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid parameters. Try commit commitHash commitName");
        }
    }
}
