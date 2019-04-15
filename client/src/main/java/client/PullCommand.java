package client;

public class PullCommand extends Command{
    public PullCommand() {}

    @Override
    public void process(String[] input) throws Exception {
        ClientService.pullRepository();
    }
}
