package client;

import java.io.IOException;

public class PushCommand extends Command {

    public PushCommand() {}

    @Override
    public void process(String[] input) throws Exception {
        ClientService.pushRepository(ClientUtils.readRepository());
    }
}