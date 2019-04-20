package client;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        CommandBuilder commandBuilder = new CommandBuilder(args);
        commandBuilder.findCommandByName().process(args);

    }
}
