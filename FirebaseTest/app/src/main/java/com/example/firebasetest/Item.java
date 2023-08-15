package com.example.firebasetest;

public class Item {
    private String imageUrl;
    private String id;
    private Integer price;
    private String category;
    private String info;
    private String seller;
<<<<<<< Updated upstream
    private String time;
=======
    private String title;
    private String futureMillis;
    private String futureDate;
>>>>>>> Stashed changes

    public Item(){

    }
<<<<<<< Updated upstream
    public Item(String imageUrl, String id, Integer price, String category, String info, String time){
=======
    public Item(String title, String imageUrl, String id, String price, String category, String info, String seller, String futureMillis, String futureDate){
        this.title = title;
>>>>>>> Stashed changes
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
<<<<<<< Updated upstream
        this.seller = getSeller();
        this.time = time;
    }
=======
        this.seller = seller;
        this.futureDate = futureDate;
        this.futureMillis = futureMillis;
    }

    public Item(String title, String imageUrl, String id, String category, String info, String seller, String futureMillis, String futureDate){ // 나눔 아이템 생성자 오버로딩 -> 가격이 빠져있음
        this.title = title;
        this.id = id;
        this.price = "share item";
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;

        this.futureDate = futureDate;
        this.futureMillis = futureMillis;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

>>>>>>> Stashed changes
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

    public String getFutureMillis() {
        return futureMillis;
    }

    public void setFutureMillis(String futureMillis) {
        this.futureMillis = futureMillis;
    }

    public String getInfo() {
        return info;
    }

    public String getFutureDate() {
        return futureDate;
    }

    public void setFutureDate(String futureDate) {
        this.futureDate = futureDate;
    }

    public void setInfo(String info) {
        this.info = info;
    }

<<<<<<< Updated upstream
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
=======
>>>>>>> Stashed changes
}
