package client.commands;

import client.service.Log;

public class LogCommand implements Command {

    public void process(String[] input) throws Exception {
        Log.log();
    }
}
