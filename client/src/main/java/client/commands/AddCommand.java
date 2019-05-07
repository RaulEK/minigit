package client.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCommand implements Command {

    private List<String> filesToAdd = new ArrayList<>();

    @Override
    public void process(String[] input) throws Exception {
        if (input.length == 2) {
            if (input[1].equals(".")) {
                filesToAdd.add("all");
            } else {
                filesToAdd.addAll(Arrays.asList(input[1].split(";")));
            }
        } else {
            System.out.println("Invalid parameters, try: add <file1;file2>");
        }
    }

    public List<String> getFilesToAdd() {
        return filesToAdd;
    }

    public void setFilesToAdd(List<String> files) {
        this.filesToAdd = files;
    }
}
