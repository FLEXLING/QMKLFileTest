package com.best.huge.qmklfiletest;

import java.util.Comparator;

public class PinyinComparator implements Comparator<FileItem>{

    public int compare(FileItem o1,FileItem o2){
        if(o1.getLetters().equals("@")||o2.getLetters().equals("#")){
            return 1;
        }else if(o1.getLetters().equals("#")||o2.getLetters().equals("@")){
            return -1;
        }else{
            return o1.getLetters().compareTo(o2.getLetters());
        }
    }
}
