package client;

import java.io.IOException;

public class Command {

    private String[] input;

    public Command(String[] input) {
        this.input = input;
    }

    public String[] getInput() {
        return input;
    }

    public void process() throws IOException {
    }
}
