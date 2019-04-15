package client;

import java.io.IOException;

public class PushCommand extends Command {

    public PushCommand() {}


    @Override
    public void process(String[] input) throws IOException {
        try {
            ClientService.pushRepository(ClientService.readRepository());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}