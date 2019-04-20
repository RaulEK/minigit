package client.service;

import models.Commit;
import models.Repository;

import java.io.IOException;
import java.util.Scanner;

public class CommentOnCommit {
    public static void CommentOnCommit(String commitHash) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("File: ");
            String fileName = scanner.nextLine();
            System.out.println("Line: ");
            int line = Integer.parseInt(scanner.nextLine());
            System.out.println("Comment: ");
            String comment = scanner.nextLine();

            Repository repo = ClientUtils.readRepository();

            Commit changedCommit = ClientUtils.getCommit(commitHash, repo);

            changedCommit.addComment(fileName, line, comment);

            ClientUtils.saveRepository(repo);
        }
    }
}
