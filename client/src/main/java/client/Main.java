package client;

import client.commands.CommandBuilder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        CommandBuilder cb = new CommandBuilder(args);
        cb.findCommandByName().process(args);
    }
}
