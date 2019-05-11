package client.commands;

import client.service.CommentOnCommit;


public class CommentOnCommitCommand implements Command{

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            CommentOnCommit.commentOnCommit(input[1], input[2], Integer.parseInt(input[3]), input[4]);
        } else {
            System.out.println("Invalid parameters. Try: comment <commitHash> <fileName> <line> <comment>");
        }
    }
}
