package com.example.firebasetest;
import  java.io.Serializable;
public class BiddingItem implements Serializable {
    private String imageUrl;
    private String id;
    private String category;
    private String info;
    private String seller;

    public BiddingItem(){

    }
    public BiddingItem(String imageUrl, String id, String category, String info, String seller){
        this.id = id;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = getSeller();
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
}
