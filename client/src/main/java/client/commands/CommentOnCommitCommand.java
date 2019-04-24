package client.commands;

import client.service.CommentOnCommit;


public class CommentOnCommitCommand implements Command{

    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            CommentOnCommit.CommentOnCommit(input[1]);
        } else {
            System.out.println("Invalid parameters. Try: comment <message>");
        }
    }
}
