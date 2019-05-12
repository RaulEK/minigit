package client.commands;

import client.service.AddAndRemove;
import client.service.ClientUtils;
import org.eclipse.jgit.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class RemoveCommand implements Command {

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            List<String> filesToAdd = Files.readAllLines(Path.of(ClientUtils.seekRepoRootFolder() + "/.minigit/addedfiles.txt"));
            String[] filesToRemove  = input[1].split(",");
            System.out.println(Arrays.toString(filesToRemove));
            for (String file :filesToRemove) {
                filesToAdd.remove(file);
            }
            if (new File(ClientUtils.seekRepoRootFolder() + "/.minigit/addedfiles.txt").delete()) {
                AddAndRemove.addFilesToTxt(StringUtils.join(filesToAdd, ","));
            }
        } else {
            System.out.println("Try: remove <relativePathToFile>");
        }
    }
}
