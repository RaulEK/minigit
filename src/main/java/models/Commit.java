package models;

import client.ZipUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Commit {
    // SHA512 Hash of zip file and some pseudorandomness added in
    private String hash;
    private String message;


    public Commit(String hash, String message) {
        this.hash = hash;
        this.message = message;
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
}


