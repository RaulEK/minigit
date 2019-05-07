package client;

import client.commands.CommandBuilder;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        CommandBuilder commandBuilder = new CommandBuilder();

        while (true) {
            System.out.print("Command: ");
            String request = sc.nextLine();
            if (request.equals("exit")) {
                break;
            }
            commandBuilder.findCommandByName(request.split(" ")).process(request.split(" "));

        }
    }
}
