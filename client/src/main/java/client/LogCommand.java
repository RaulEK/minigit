package client;

import java.io.IOException;

public class LogCommand extends Command {
    @Override
    public void process(String[] input) throws Exception {
        ClientService.log();
    }
}
