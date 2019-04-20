package client.commands;

import client.service.PullRepository;

public class PullCommand extends Command {
    public PullCommand() {}

    @Override
    public void process(String[] input) throws Exception {
        PullRepository.pullRepository();
    }
}
