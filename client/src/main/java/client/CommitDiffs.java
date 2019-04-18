package client;

import java.io.IOException;

public class CommitDiffs extends Command {
    @Override
    public void process(String[] input) throws Exception {
        setInput(input);
        if(getInput().length == 2) {
            ClientService.commitDiffs(getInput()[1]);
        } else {
            System.out.println("Invalid parameters. Try diff commitHash");
        }

    }
}
