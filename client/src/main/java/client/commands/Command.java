package client.commands;

public interface Command {

    void process(String[] input) throws Exception;
}
