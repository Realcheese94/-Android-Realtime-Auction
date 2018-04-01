package com.example.sj.gohigh;

/**
 * Created by sj on 2017-12-11.
 */

public class ChatData {
    private String userName;
    private String message;
    public ChatData(){

    }
    public ChatData(String userName,String message){
        this.userName = userName;
        this.message = message;
    }
    public String getUserName(){
        return userName;
    }
    public String getMessage(){
        return message;
    }
}
