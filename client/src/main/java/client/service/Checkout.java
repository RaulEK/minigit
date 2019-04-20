package client.service;

import models.ZipUtils;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.nio.file.Paths;

public class Checkout {
    public static void checkout(String commitHash) throws ZipException, IOException {
        ClientUtils.cleanWorkingDirectory();

        ZipUtils.extractZipFile(String.valueOf(ClientUtils.seekMinigitFolder().resolve(commitHash + ".zip")), String.valueOf(ClientUtils.seekRepoRootFolder()));
    }
}
