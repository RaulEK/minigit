package client;

import java.io.IOException;

public class CheckoutCommand extends Command {
    @Override
    public void process(String[] input) throws Exception {
        if(input.length == 2) {
            ClientService.checkout(input[1]);
        } else {
            System.out.println("Invalid parameters. Try checkout commitHash.");
        }

    }
}
