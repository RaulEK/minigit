package client.commands;

import server.Server;

public class ServerCommand implements Command {
    @Override
    public void process(String[] input) throws Exception {
        if(input.length == 1) {
            Server.main(null);
        }
    }
}
