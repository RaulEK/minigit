package client.commands;

import client.service.Branch;
import client.service.Checkout;
import models.Utils;
import models.Commit;

import java.util.ArrayList;

public class CheckoutCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if(input.length == 2) {
            Checkout.checkout(input[1]);
        } else if(input.length == 3) {
            if(input[1].equals("-b")) {
                if (Branch.switchToBranch(input[2])) {
                    ArrayList<Commit> repo = Utils.readRepository().getCommits();
                    Checkout.checkout(repo.get(repo.size() - 1).getHash());
                }
            } else {
                System.out.println("Invalid command");
            }
        } else {
            System.out.println("Invalid parameters. Try: checkout [-b] <name>");
        }
    }
}
