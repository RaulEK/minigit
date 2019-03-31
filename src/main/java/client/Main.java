package client;

public class Main {

    public static void main(String[] args) throws Exception {
        CommandBuilder command = new CommandBuilder(args);
        command.execute();
    }
}