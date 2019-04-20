package client;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IgnoreCommand extends Command {

    public IgnoreCommand() {
    }

    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            try {
                byte[] bytes = (input[1]).getBytes(StandardCharsets.UTF_8);
                Files.createFile(Paths.get(".minigitignore"));
                Files.write(Path.of(".minigitignore"), bytes, StandardOpenOption.APPEND);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid parameters. Try: ignore <file name with relative path>");
        }
    }
}
