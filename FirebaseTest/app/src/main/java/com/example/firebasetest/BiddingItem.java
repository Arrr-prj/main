package com.example.firebasetest;
import  java.io.Serializable;
public class BiddingItem implements Serializable {
    private String title;
    private String imageUrl;
    private String id;
    private String category;
    private String info;
    private String seller;
    private String time;
    private String price;

    public BiddingItem(){    }
    public BiddingItem(String title, String imageUrl, String price, String id, String category, String info, String seller){
        this.title = title;
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int profile) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public  String getTime(){return  time;}

    public void setTime(String time){this.time = time;}
}