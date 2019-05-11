package client.service;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import models.Constants;
import models.ZipUtils;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommitDiffs {
    public static void commitDiffs(String commitHash, String ancestorHash, Boolean print) throws IOException, ZipException, DiffException {

        /* Temporary directory where files are held while compared. */
        Path tempDir = Paths.get(ClientUtils.seekMinigitFolder().toString(), ".tempCommits");

        /* Creates .tempCommits if it does't exist */
        if(!Files.exists(tempDir)) {
            Files.createDirectory(tempDir);
        }

        /* Commits zip path */
        String commitZipPath = Paths.get(ClientUtils.seekMinigitFolder().toString(), commitHash + ".zip").toString();
        String commitAncestorZipPath;
        String commitTemp = Paths.get(tempDir.toString(), commitHash).toString();;
        String ancestorTemp = null;
        List<String> commitDirPaths = new ArrayList<>();
        List<String> ancestorDirPaths = new ArrayList<>();
        Map<String, Integer> files = new HashMap<>();

        if (ancestorHash != null && !ancestorHash.equals("")) {
            commitAncestorZipPath = Paths.get(ClientUtils.seekMinigitFolder().toString(), ancestorHash + ".zip").toString();

            Files.createDirectory(Paths.get(tempDir.toString(), commitHash));

            Files.createDirectory(Paths.get(tempDir.toString(), ancestorHash));

            ancestorTemp = Paths.get(tempDir.toString(), ancestorHash).toString();

            ZipUtils.extractZipFile(commitAncestorZipPath, ancestorTemp);

            ClientUtils.getAllFilePathsInDir(new File(ancestorTemp), ancestorDirPaths);
        }

        ZipUtils.extractZipFile(commitZipPath, commitTemp);
        ClientUtils.getAllFilePathsInDir(new File(commitTemp), commitDirPaths);

        HashMap<String, HashMap<Integer, String>> diffComments = ClientUtils.getCommit(commitHash, ClientUtils.readRepository()).getDiffComments();

        ClientUtils.findRemovedFiles(commitDirPaths, ancestorDirPaths, files);

        ClientUtils.findAddedFiles(commitDirPaths, ancestorDirPaths, files);

        for (Map.Entry<String, Integer> entry : files.entrySet()) {
            if(entry.getValue() == Constants.REMOVED_FILE) {
                System.out.println("__________________________________________________");
                System.out.println("File deleted --- " + Constants.ANSI_RED + entry.getKey() + Constants.ANSI_RESET);
            } else if(entry.getValue() == Constants.ADDED_FILE) {
                System.out.println("__________________________________________________");
                System.out.println("New file --- " + Constants.ANSI_GREEN + entry.getKey() + Constants.ANSI_RESET);
                try {
                    List<String> lines = Files.readAllLines(Paths.get(commitTemp, entry.getKey()));
                    for (int i = 0; i < lines.size(); i++) {
                        System.out.println((i + 1) + "| " + Constants.ANSI_GREEN + lines.get(i) + Constants.ANSI_RESET);

                        checkForComments(diffComments, entry, i);
                    }
                } catch (MalformedInputException e) {
                    continue;
                }

                System.out.println("__________________________________________________");
            } else if (entry.getValue() == Constants.CHANGED_FILE) {
                List<String> original;
                List<String> patched;
                try {
                    original = Files.readAllLines(new File(ancestorTemp + File.separator + entry.getKey()).toPath());
                    patched = Files.readAllLines(new File(commitTemp + File.separator + entry.getKey()).toPath());
                } catch (MalformedInputException e) {
                    continue;
                }

                if (DiffUtils.diff(original, patched).getDeltas().isEmpty()) {
                    continue;
                }

                System.out.println("__________________________________________________");

                System.out.println(entry.getKey() + "\n");

                DiffRowGenerator generator = DiffRowGenerator.create()
                        .showInlineDiffs(true)
                        .ignoreWhiteSpaces(true)
                        .oldTag(f -> "")
                        .newTag(f -> "")
                        .build();
                List<DiffRow> rows = generator.generateDiffRows(original, patched);

                if(print) {
                    for (int i = 0; i < rows.size(); i++) {
                        DiffRow row = rows.get(i);
                        if(!row.getNewLine().equals(row.getOldLine())) {
                            if(row.getOldLine() != "") {
                                System.out.println((i + 1) + "| " + Constants.ANSI_RED + row.getOldLine() + Constants.ANSI_RESET);
                            }
                            System.out.println((i + 1) + "| " + Constants.ANSI_GREEN + row.getNewLine() + Constants.ANSI_RESET);
                        } else {
                            System.out.println((i + 1) + "| " + row.getNewLine());
                        }
                        checkForComments(diffComments, entry, i);
                    }
                    System.out.println("__________________________________________________");
                } else {
                }


            }
        }

        ClientUtils.deleteDirectory(new File(commitTemp));
        if (ancestorTemp != null) {
            ClientUtils.deleteDirectory(new File(ancestorTemp));
        }
    }

    private static void checkForComments(HashMap<String, HashMap<Integer, String>> diffComments, Map.Entry<String, Integer> entry, int i) {
        if (diffComments.containsKey(entry.getKey())) {
            if (diffComments.get(entry.getKey()).containsKey(i + 1)) {
                System.out.println("<<< Comment: " + diffComments.get(entry.getKey()).get(i + 1) + " >>>");
            }
        }
    }
}
