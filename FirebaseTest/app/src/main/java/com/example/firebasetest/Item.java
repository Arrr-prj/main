package com.example.firebasetest;

public class Item {
    private String imageUrl;
    private String id;
    private String price;
    private String category;
    private String info;
    private String seller;
    private String title;
    private String futureDate;
    private String futureMillis;

    public Item() {

    }

    public Item(String title, String imageUrl, String id, String price, String category, String info, String seller, String futureMillis, String futureDate) {
        this.title = title;
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
        this.futureMillis = futureMillis;
        this.futureDate = futureDate;
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

    public String getfutureDate() { return futureDate; }

    public void setfutureDate(String futureDate) {
        this.futureDate = futureDate;
    }

    public String getfutureMillis(){return futureMillis;}

    public void setfutureMillis(String futureMillis){this.futureMillis = futureMillis;}
}
