package com.example.firebasetest;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 알림 받는 클래스입니다.
public class Notification {
    private String title;
    private String message;
    private String time; // "2023-08-17 16:07:28" 형식의 문자열

    private Date timestamp; // 변환한 시간을 저장할 필드
    private String itemTitle;
    private String itemType;

    public Notification(String title, String message, String time, String itemTitle, String itemType) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.itemTitle = itemTitle;
        this.itemType = itemType;

        // "yyyy-MM-dd HH:mm:ss" 형식의 문자열을 Date 객체로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            this.timestamp = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
    public Date getTimestamp() {return timestamp;}

    public void setTimestamp(Date timestamp) {this.timestamp = timestamp;}
    public void setTitle(String title) { this.title = title; }
    public void setMessage(String message) { this.message = message; }
    public void setTime(String time) { this.time = time; }
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public void setItemType(String itemType) {this.itemType = itemType;}

    public String getItemTitle() {return itemTitle;}

    public String getItemType() {return itemType;}
}