package client;

import java.io.IOException;

public class Command {

    private String[] input;

    public Command() {
    }

    public String[] getInput() {
        return input;
    }

    public void setInput(String[] input) {
        this.input = input;
    }

    public void process(String[] input) throws IOException {
    }
}
