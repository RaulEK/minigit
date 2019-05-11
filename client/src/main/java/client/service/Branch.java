package client.service;

import models.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Branch {

    public static void createBranch(String branchName) throws IOException {

        Repository branch = new Repository(branchName, false);

        Path pathToBranchFile = Paths.get(".minigit", "branches", branchName + ".json").normalize();

        if(!Files.exists(pathToBranchFile)) {
            ClientUtils.createFolder(String.valueOf(Paths.get(".minigit", "branches")));
        }

        ClientUtils.createRepositoryJsonFile(branch, pathToBranchFile);
    }



    public static boolean switchToBranch(String branchName) throws IOException {
        Path pathToCurrentBranch;
        Path pathToBranch;

        if (!ClientUtils.findCurrentBranchJsonFileName().equals(branchName + ".json")) {
            pathToCurrentBranch = Paths.get(String.valueOf(ClientUtils.seekMinigitFolder()), ClientUtils.findCurrentBranchJsonFileName());
            pathToBranch =  Paths.get(String.valueOf(ClientUtils.seekMinigitFolder()), "branches", branchName + ".json").normalize();
        } else {
            System.out.println("Already in " + branchName);
            return false;
        }

        if(!Files.exists(pathToBranch)) {
            System.out.println("Branch does not exist.");
        }

        File current = new File(String.valueOf(pathToCurrentBranch));
        File branch = new File(String.valueOf(pathToBranch));

        current.renameTo(new File(String.valueOf(pathToCurrentBranch.getParent().resolve("branches").resolve(pathToCurrentBranch.getFileName()))));

        branch.renameTo(new File(String.valueOf(pathToBranch.getParent().getParent().resolve(branchName + ".json"))));
        return true;
    }
}
