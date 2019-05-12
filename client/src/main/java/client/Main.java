package client;

import client.commands.CommandBuilder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        CommandBuilder commandBuilder = new CommandBuilder(args);
        commandBuilder.findCommandByName().process(args);
    }
}
