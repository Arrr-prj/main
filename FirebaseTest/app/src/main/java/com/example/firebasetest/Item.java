package com.example.firebasetest;

public class Item {
    private String imageUrl;
    private String id;
    private Integer price;
    private String category;
    private String info;
    private String seller;

    public Item(){

    }
    public Item(String imageUrl, String id, Integer price, String category, String info, String seller){
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
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
