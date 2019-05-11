package client.commands;

import client.service.ClientUtils;
import client.service.CommitDiffs;

public class CommitDiffsCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if(input.length == 2) {
            CommitDiffs.commitDiffs(input[1], ClientUtils.getAncestorOfHash(input[1]), true);
        } else {
            System.out.println("Invalid parameters. Try diff <commitHash>");
        }

    }
}
