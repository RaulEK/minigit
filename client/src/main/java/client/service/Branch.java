package client.service;

import models.Utils;
import models.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Branch {

    public static void createBranch(String branchName) throws IOException {

        Repository branch = new Repository(branchName, false, Utils.readRepository().getHost() + ":" + Utils.readRepository().getPort());

        branch.setCommits(Utils.readRepository().getCommits());

        Path pathToBranchFile = Paths.get(".minigit", "branches", branchName + ".json").normalize();

        if(!Files.exists(pathToBranchFile)) {
            Utils.createFolder(String.valueOf(Paths.get(".minigit", "branches")));
        }

        Utils.createRepositoryJsonFile(branch, pathToBranchFile);
    }



    public static boolean switchToBranch(String branchName) throws IOException {
        Path pathToCurrentBranch;
        Path pathToBranch;

        if (!Utils.findCurrentBranchJsonFileName().equals(branchName + ".json")) {
            pathToCurrentBranch = Paths.get(String.valueOf(Utils.seekMinigitFolder()), Utils.findCurrentBranchJsonFileName());
            pathToBranch =  Paths.get(String.valueOf(Utils.seekMinigitFolder()), "branches", branchName + ".json").normalize();
        } else {
            System.out.println("Already in " + branchName);
            return false;
        }

        if(!Files.exists(pathToBranch)) {
            System.out.println("Branch does not exist.");
        }

        Files.move(pathToCurrentBranch, pathToCurrentBranch.getParent().resolve("branches").resolve(pathToCurrentBranch.getFileName()));
        Files.move(pathToBranch, pathToBranch.getParent().getParent().resolve(branchName + ".json"));

        return true;
    }
}
