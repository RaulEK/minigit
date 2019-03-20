package client;

import com.google.gson.Gson;
import models.Commit;
import models.Constants;
import models.Repository;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


public class ClientService {

    public static void pushRepository(Repository repo) throws IOException {

        // here we should:
        // zip the entire .minigit folder
        // send it to the server
        // in the server we should unpack it.
        // and attempt to store it
        // if there are any conflicts (commit zips with same name) then reject

        /* String temporaryArchiveName = UUID.randomUUID().toString();

        String fs = System.getProperty("file.separator");

        //Sends Repo zip to server.
        System.out.println("Connecting to server.");
        try (Socket socket = new Socket("localhost", 8080);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            // TODO: zip archive here and send it to server
            byte[] bytes = Files.readAllBytes(Paths.get("" +  temporaryArchiveName));

            Gson gson = new Gson();

            String json = gson.toJson(repo);

            // Message type

            // outputStream.writeInt(1);

            // outputStream.writeUTF(json);

        }
        System.out.println("Connection finished.");*/
    }

    public static void commitRepository(String commitName) {

        // here we should:
        // add the entire working directory to zip file
        // excluding the .minigit folder
        // name the zip some substring of the file's hash and add some pseudorandom value
        // create a commit with the given message and zip file name (without extension)
        // add the commit to the repository
        // save the repository file

        /*// Saves zip to local repo folder.
        Path projectPath = Paths.get("");

        String fs = System.getProperty("file.separator");

        Commit commit = new Commit(commitName);


        String temporaryArchiveName = UUID.randomUUID().toString();

        // create a zip of everything in directory, ignoring the .minigit folder
        ZipUtils appZip = new ZipUtils(Paths.get(""), Paths.get(Constants.MINIGIT_DIRECTORY_NAME, temporaryArchiveName+".zip"));
        appZip.generateFileList(new File(appZip.getSOURCE_FOLDER()));
        appZip.zipIt(appZip.getOUTPUT_ZIP_FILE());*/
    }

    public static Repository readRepository() {
        // here we should:
        // read the repository file from .minigit folder
        // will use this when we need access to the repository object
    }

    public static void saveRepository(Repository repository) {
        // here we should:
        // serialize the repository
        // save the repository file to .minigit folder
    }
}
