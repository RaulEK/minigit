
import client.service.*;
import com.github.difflib.algorithm.DiffException;
import models.Commit;
import models.Repository;
import net.lingala.zip4j.exception.ZipException;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestClientService {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Test
    public void testCommitDiffs() throws IOException, ZipException, DiffException, InterruptedException {
        Init.initRepository("test");

        CommitRepository.commitRepository("first commit");

        Path testText = Paths.get("src", "test", "resources", "test.txt");

        try (PrintWriter pw = new PrintWriter(testText.toString())) {
            pw.println("some new line");
        }

        CommitRepository.commitRepository("new file");

        Repository repo = ClientUtils.readRepository();
        List<Commit> commits = repo.getCommits();

        String lastCommitHash = commits.get(commits.size() - 1).getHash();

        CommentOnCommit.commentOnCommit(lastCommitHash, testText.toString(), 1, "great code");

        System.setOut(new PrintStream(outContent));

        CommitDiffs.commitDiffs(lastCommitHash, ClientUtils.getAncestorOfHash(lastCommitHash), true);

        try {
            Assert.assertEquals("__________________________________________________\n" +
                            "New file --- \u001B[32msrc/test/resources/test.txt\u001B[0m\n" +
                            "1| \u001B[32msome new line\u001B[0m\n" +
                            "<<< Comment: great code >>>\n" +
                            "__________________________________________________\n"
                    , outContent.toString());
        } finally {
            System.setOut(originalOut);
            ClientUtils.deleteDirectory(new File(".minigit"));
            Files.delete(testText);
        }
    }
}
