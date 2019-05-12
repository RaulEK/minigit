package client.service;

import models.Utils;
import models.Commit;
import models.Repository;

import java.io.IOException;

public class CommentOnCommit {
    public static void commentOnCommit(String commitHash, String fileName, int line, String comment) throws IOException {
            Repository repo = Utils.readRepository();

            Commit changedCommit = Utils.getCommit(commitHash, repo);

            changedCommit.addComment(fileName, line, comment);

            Utils.saveRepository(repo);
    }
}
