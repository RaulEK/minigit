package models;


public class Commit {
    // SHA512 Hash of zip file and some pseudorandomness added in
    private String hash;
    private String message;
    private String ancestorHash;


    public Commit(String hash, String message, String ancestorHash) {
        this.hash = hash;
        this.message = message;
        this.ancestorHash = ancestorHash;
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

    public String getAncestorHash() { return ancestorHash; }
}


