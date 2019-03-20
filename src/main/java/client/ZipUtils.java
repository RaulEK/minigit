package client;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// Creating a zip file from directory.
public class ZipUtils {

    // need to comment this, rename variables to lowercase
    private List<String> fileList;
    private String OUTPUT_ZIP_FILE;
    private String SOURCE_FOLDER; // SourceFolder path

    public ZipUtils(Path SOURCE_FOLDER, Path OUTPUT_ZIP_FILE) {
        fileList = new ArrayList< String >();
        this.OUTPUT_ZIP_FILE = OUTPUT_ZIP_FILE.toString();
        this.SOURCE_FOLDER = SOURCE_FOLDER.toString();

    }

    public String getOUTPUT_ZIP_FILE() {
        return OUTPUT_ZIP_FILE;
    }

    public String getSOURCE_FOLDER() {
        return SOURCE_FOLDER;
    }

    // zips a directory
    public void zipIt(String zipDirectory) {
        byte[] buffer = new byte[1024];
        String source = new File(SOURCE_FOLDER).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipDirectory);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipDirectory);
            FileInputStream in = null;

            for (String file: this.fileList) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            System.out.println("Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }
}
