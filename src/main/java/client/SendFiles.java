package client;

import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SendFiles {
    public static void main(String[] args) throws Exception {

        String projectDirectory = args[0]; //Directory
        Path projectPath = Paths.get(projectDirectory);

        String repo; // host, args[1]

        List<String> files = new ArrayList<String>(); //Files to send.

        //Reads all files from directory
        try(Stream<Path> walk = Files.walk(projectPath)) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            for (String file: result) {
                files.add(file);
            }
        }

        System.out.println("Connecting to server.");
        try (Socket socket = new Socket("localhost", 8080);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            int messageType = 1; // type, dir. name len, directory name, file name len, file name, file len , file bytes, file name len, ....
            outputStream.writeInt(messageType);

            String dirName = projectPath.getFileName().toString();

            outputStream.writeUTF(dirName);

            // Read and send file names and files to server
            for(String file: files) {
                File fileToSend = new File(file);

                Path path = fileToSend.toPath();

                String fileName = path.getFileName().toString();

                outputStream.writeUTF(fileName);

                byte[] fileBytes = Files.readAllBytes(path);

                outputStream.writeInt(fileBytes.length);
                outputStream.write(fileBytes);
            }

        }
        System.out.println("Connection finished.");
    }
}
