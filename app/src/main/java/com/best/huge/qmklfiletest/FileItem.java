package com.best.huge.qmklfiletest;

public class FileItem{

    public FileItem(String name,String size){
        this.name=name;
        this.size=size;
    }

    private String size;
    private String name;
    private String letters;

    public void setSize(String size){
        this.size=size;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setLetters(String letters){
        this.letters=letters;
    }

    public String getSize(){
        return size;
    }

    public String getName(){
        return name;
    }

    public String getLetters(){
        return letters;
    }
}
