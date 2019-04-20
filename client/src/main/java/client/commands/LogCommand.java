package client.commands;

import client.service.Log;

public class LogCommand extends Command {
    @Override
    public void process(String[] input) throws Exception {
        Log.log();
    }
}
