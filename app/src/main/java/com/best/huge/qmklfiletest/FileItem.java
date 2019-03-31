package com.best.huge.qmklfiletest;

public class FileItem {

    public void setSize(String size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String size;
    private String name;

    public FileItem(String name,String size){
        this.name=name;
        this.size=size;
    }

    public String getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

}
