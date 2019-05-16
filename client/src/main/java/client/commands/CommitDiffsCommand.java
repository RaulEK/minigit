package client.commands;

import models.Utils;
import client.service.CommitDiffs;

public class CommitDiffsCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if(input.length == 2) {
            CommitDiffs.commitDiffs(input[1], Utils.getAncestorOfHash(input[1]));
        } else {
            System.out.println("Invalid parameters. Try diff <commitHash>");
        }

    }
}
