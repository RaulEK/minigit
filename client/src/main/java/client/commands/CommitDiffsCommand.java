package client.commands;

import client.service.CommitDiffs;

public class CommitDiffsCommand extends Command {
    @Override
    public void process(String[] input) throws Exception {
        if(input.length == 2) {
            CommitDiffs.commitDiffs(input[1]);
        } else {
            System.out.println("Invalid parameters. Try diff <commitHash>");
        }

    }
}
