package models;


import java.util.HashMap;

public class Commit {
    // SHA512 Hash of zip file and some pseudorandomness added in
    private String hash;
    private String message;
    private String ancestorHash;
    /* HashMap that contains commit diff comments.
     * FileName: [line: message] */
    private HashMap<String, HashMap<Integer, String>> diffComments;


    public Commit(String hash, String message, String ancestorHash) {
        this.hash = hash;
        this.message = message;
        this.ancestorHash = ancestorHash;
        this.diffComments = new HashMap<>();
    }

    public String getHash() {
        return hash;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, HashMap<Integer, String>> getDiffComments() {
        return diffComments;
    }

    public String getAncestorHash() { return ancestorHash; }

    public void addComment(String fileName, int line, String comment) {
        HashMap<Integer, String> lineComment;
        if (diffComments.containsKey(fileName)) {
            lineComment = diffComments.get(fileName);
        } else {
            lineComment = new HashMap();
        }
        lineComment.put(line, comment);
        diffComments.put(fileName, lineComment);
    }

}


