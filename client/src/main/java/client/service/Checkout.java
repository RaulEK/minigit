package client.service;

import models.Utils;
import models.ZipUtils;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;

public class Checkout {
    public static void checkout(String commitHash) throws ZipException, IOException {
        Utils.cleanWorkingDirectory();

        ZipUtils.extractZipFile(String.valueOf(Utils.seekMinigitFolder().resolve(commitHash + ".zip")), String.valueOf(Utils.seekRepoRootFolder()));
    }
}
