package models;

import java.net.Proxy;
import java.util.ArrayList;

public class Repository {
    private String name;
    private boolean isMaster;
    private String host;
    private int port;

    private ArrayList<Commit> commits = new ArrayList<>();

    public Repository(String name, boolean isBranch, String host) {
        this.name = name;
        this.isMaster = isBranch;
        this.host = host.split(":")[0];
        this.port = Integer.parseInt(host.split(":")[1]);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setCommits(ArrayList<Commit> commits) {
        this.commits = commits;
    }

    public ArrayList<Commit> getCommits() {
        return commits;
    }

    public void addCommit(Commit commit) {
        commits.add(commit);
    }

    public String getName() {
        return name;
    }

}
