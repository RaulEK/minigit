package client;

import client.commands.CommandBuilder;

public class Main {

    public static void main(String[] args) throws Exception {

        CommandBuilder cb = new CommandBuilder(args);
        cb.findCommandByName().process(args);
    }
}
