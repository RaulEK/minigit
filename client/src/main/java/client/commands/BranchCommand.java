package client.commands;

import client.service.Branch;
import models.Utils;


public class BranchCommand implements Command {
    public BranchCommand() {
    }

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 1) {
            System.out.println(Utils.findCurrentBranchJsonFileName().split("\\.")[0]);
        } else if (input.length == 2) {
            if (input[1].equals("-a")) {
                System.out.println(Utils.findCurrentBranchJsonFileName().split("\\.")[0]);
                for (String branchName : Utils.findAllBranchNames()) {
                    if (branchName.endsWith(".json"))
                        System.out.println(branchName.split("\\.")[0]);
                }
                return;
            }
            Branch.createBranch(input[1]);
        } else {
            System.out.println("Invalid parameters. Try: branch [<branchName>]");
        }
    }
}
