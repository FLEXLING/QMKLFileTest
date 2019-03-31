package com.best.huge.qmklfiletest;

public class FileRequest {
    String path;
    String token;
    String collegeName;

    public FileRequest(String path, String collegeName, String token) {
        this.token = token;
        this.path = path;
        this.collegeName = collegeName;
    }
}
