package com.example.firebasetest;

public class Announcement {
    private String title;
    private String contents;
    private String uploadMillis;

    private String differenceMillis;

    public Announcement(String title, String contents, String uploadMillis, String differenceMillis){
        this.title = title;
        this.contents = contents;
        this.uploadMillis = uploadMillis;
        this.differenceMillis = differenceMillis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadMillis() {
        return uploadMillis;
    }

    public void setUploadMillis(String uploadMillis) {
        this.uploadMillis = uploadMillis;
    }

    public String getDifferenceMillis() {
        return differenceMillis;
    }

    public void setDifferenceMillis(String differenceMillis) {
        this.differenceMillis = differenceMillis;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
