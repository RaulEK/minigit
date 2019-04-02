package client;

import java.io.IOException;

public class PushCommand extends Command {

    public PushCommand(String[] input) {
        super(input);
    }

    @Override
    public void process() throws IOException {
        try {
            ClientService.pushRepository(ClientService.readRepository());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
