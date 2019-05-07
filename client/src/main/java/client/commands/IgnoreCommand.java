package client.commands;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IgnoreCommand implements Command {

    public IgnoreCommand() {
    }

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            try {
                byte[] bytes = (input[1] + "\n").getBytes(StandardCharsets.UTF_8);
                if(!Files.exists(Paths.get(".minigitignore"))) {
                    Files.createFile(Paths.get(".minigitignore"));
                }
                Files.write(Path.of(".minigitignore"), bytes, StandardOpenOption.APPEND);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid parameters. Try: ignore <relativeFilePath>");
        }
    }
}
