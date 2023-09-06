package com.example.firebasetest;

import java.io.Serializable;

public class ChatData {

    private String msg;
    private String name;
    private String to;
    private String sendTime;

    public ChatData(String msg, String name, String to, String sendTime){
        this.msg = msg;
        this.name = name;
        this.to = to;
        this.sendTime = sendTime;
    }

    public ChatData(){
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
