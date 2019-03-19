package client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Object that will hold repository zip, history and other information.
public class Repository {
    private String name;
    private byte[] zip;
    private List<String> commitHistory = new ArrayList<>();

    public Repository(String name, byte[] zip) {
        this.name = name;
        this.zip = zip;
    }

    public List<String> getCommitHistory() {
        return commitHistory;
    }

    public String getName() {
        return name;
    }

    public byte[] getZip() {
        return zip;
    }
}
