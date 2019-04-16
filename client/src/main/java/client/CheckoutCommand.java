package client;

import java.io.IOException;

public class CheckoutCommand extends Command {
    @Override
    public void process(String[] input) throws Exception {
        setInput(input);
        if(getInput().length == 2) {
            ClientService.checkout(getInput()[1]);
        } else {
            System.out.println("Invalid parameters. Try checkout commitHash.");
        }

    }
}
