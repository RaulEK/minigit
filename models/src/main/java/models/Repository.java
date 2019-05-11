package models;

import java.util.ArrayList;

public class Repository {
    private String name;
    private Boolean isMaster;

    private ArrayList<Commit> commits = new ArrayList<>();

    public Repository(String name, Boolean isBranch) {
        this.name = name;
        this.isMaster = isBranch;
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
