package models;

import java.util.ArrayList;

public class Repository {
    private String name;
    private ArrayList<Commit> commits = new ArrayList<>();

    public Repository(String name) {
        this.name = name;
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
