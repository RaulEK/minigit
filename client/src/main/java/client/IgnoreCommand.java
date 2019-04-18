package client;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class IgnoreCommand extends Command {

    public IgnoreCommand() {}

    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            try {
                byte[] bytes = input[1].getBytes(StandardCharsets.UTF_8);
                Files.write(Path.of(".minigitignore"), bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid parameters. Try: ignore <fullFileName>");
        }
    }
}
