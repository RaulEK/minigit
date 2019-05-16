package client;

import client.commands.CommandBuilder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        CommandBuilder cb = new CommandBuilder(args);
        if (cb.findCommandByName() != null) {
            cb.findCommandByName().process(args);
        } else {
            System.out.println("Command not available. Available commands: ");
            for (String s : cb.getAvailableCommands().keySet()) {
                System.out.println(s);
            }
        }

    }
}
