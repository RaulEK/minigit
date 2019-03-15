package client;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Commit {
    public static void main(String[] args) throws Exception {

        // Saves zip to local repo folder.
        String projectDirectory = args[0]; // Repo directory on system
        Path projectPath = Paths.get(projectDirectory);

        String fs = System.getProperty("file.separator");

        ZipUtils appZip = new ZipUtils("src" + fs + "main" + fs + "java" + fs + "client" + fs + "repo" + fs + projectPath.getFileName().toString() + ".zip", projectDirectory);
        appZip.generateFileList(new File(appZip.getSOURCE_FOLDER()));
        appZip.zipIt(appZip.getOUTPUT_ZIP_FILE());
    }
}


