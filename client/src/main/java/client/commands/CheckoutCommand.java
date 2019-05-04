package client.commands;

import client.service.Checkout;

public class CheckoutCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if(input.length == 2) {
            Checkout.checkout(input[1]);
        } else {
            System.out.println("Invalid parameters. Try: checkout <commitHash>");
        }
    }
}
