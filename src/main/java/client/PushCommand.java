package client;

import java.io.IOException;

public class PushCommand extends Command {

    public PushCommand(String[] input) {
        super(input);
    }

    @Override
    public void process() throws IOException {
        ClientService.pushRepository(ClientService.readRepository());
    }

}
