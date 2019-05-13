package client.service;

import models.Utils;
import models.Commit;
import models.Repository;

import java.io.IOException;
import java.util.List;

public class Log {
    public static void log() throws IOException {
        Repository repo = Utils.readRepository();
        List<Commit> commits = repo.getCommits();

        System.out.println("\n" + "Branch >>> " + Utils.findCurrentBranchJsonFileName().split("\\.")[0]);

        for (int i = 0; i < commits.size(); i++) {
            System.out.println(i);
            System.out.println("Hash: " + commits.get(i).getHash());
            System.out.println("CommitMessage: " + commits.get(i).getMessage() + "\n");
        }
    }
}
