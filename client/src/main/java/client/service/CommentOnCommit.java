package client.service;

import models.Commit;
import models.Repository;

import java.io.IOException;
import java.util.Scanner;

public class CommentOnCommit {
    public static void commentOnCommit(String commitHash, String fileName, int line, String comment) throws IOException {
            Repository repo = ClientUtils.readRepository();

            Commit changedCommit = ClientUtils.getCommit(commitHash, repo);

            changedCommit.addComment(fileName, line, comment);

            ClientUtils.saveRepository(repo);
    }
}
