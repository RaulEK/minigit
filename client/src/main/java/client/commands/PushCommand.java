package client.commands;

import client.service.ClientUtils;
import client.service.PushRepository;

public class PushCommand implements Command {

    public PushCommand() {}

    @Override
    public void process(String[] input) throws Exception {
        PushRepository.pushRepository(ClientUtils.readRepository());
    }
}