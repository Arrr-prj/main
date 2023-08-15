package com.example.firebasetest;

public class Item {
    private String imageUrl;
    private String id;
    private String price;
    private String category;
    private String info;
    private String seller;
    private String title;

    public Item(){

    }
    public Item(String title, String imageUrl, String id, String price, String category, String info, String seller){
        this.title = title;
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
    }

    public Item(String title, String imageUrl, String id, String category, String info, String seller){ // 나눔 아이템 생성자 오버로딩 -> 가격이 빠져있음
        this.title = title;
        this.id = id;
        this.price = "share item";
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

    public void setImageUrl(String profile) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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