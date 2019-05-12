package client.commands;

import models.Utils;
import client.service.PushRepository;

public class PushCommand implements Command {

    public PushCommand() {}

    @Override
    public void process(String[] input) throws Exception {
        PushRepository.pushRepository(Utils.readRepository());
    }
}