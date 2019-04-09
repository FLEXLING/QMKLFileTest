package com.best.huge.qmklfiletest;

import java.util.HashMap;
import java.util.List;

public class Reception {


    private String code;
    private String msg;
    private HashMap<String,String> data;

    private String letter;

    public Reception() {
        code= "";
        msg= "";
        data=new HashMap<>();
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getLetters() {
        return letter;
    }
}
