package models;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.nio.file.Paths;

public final class ZipUtils {

    public static void createZipFileFromFolder(String zipPath, String folderPath) throws ZipException {
        ZipFile zip = new ZipFile(Paths.get(zipPath).toString());

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        /* Creates a zip file from the .minigit folder and reads all bytes. */
        zip.createZipFileFromFolder(folderPath, zipParameters, false, 0);

    }

    public static void extractZipFile(String path, String dest) throws ZipException {
        ZipFile zip = new ZipFile(path);
        zip.extractAll(dest);
    }
}
