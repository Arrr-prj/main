package com.example.firebasetest;

import com.google.api.Billing;

public class Item {
    private String imageUrl;
    private String id;
    private String price;
    private String category;
    private String info;
    private String seller;
    private String title;
    private String futureMillis;
    private String futureDate;
    private  String endPrice;
    private  String uploadMillis;
    private Boolean confirm;
    private String buyer;
    private String itemType;

    public String getDifferenceDays() {
        return differenceDays;
    }

    public void setDifferenceDays(String differenceDays) {
        this.differenceDays = differenceDays;
    }

    private String differenceDays;
    private Integer views;


    public Item(String title, String imageUrl, String id, String price, String endPrice, String category, String info, String seller, String buyer, String futureMillis, String futureDate, String uploadMillis, String differenceDays, Boolean confirm, String itemType, Integer views){
        this.title = title;
        this.id = id;
        this.price = price;
        this.endPrice = endPrice;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
        this.buyer = buyer;
        this.futureDate = futureDate;
        this.futureMillis = futureMillis;
        this.uploadMillis = uploadMillis;
        this.differenceDays = differenceDays;
        this.confirm = confirm;
        this.itemType = itemType;
        this.views = views;
    }

    public Item(String title, String imageUrl, String id, String price, String endPrice, String category, String info, String seller, String buyer, String futureMillis, String futureDate, String uploadMillis, String differenceDays, Boolean confirm){
        this.title = title;
        this.id = id;
        this.price = price;
        this.endPrice = endPrice;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
        this.buyer = buyer;
        this.futureDate = futureDate;
        this.futureMillis = futureMillis;
        this.uploadMillis = uploadMillis;
        this.differenceDays = differenceDays;
        this.confirm = confirm;
    }

    public Item(){

    }
    public Item(String title, String imageUrl, String id, String price, String category, String info, String seller, String futureMillis, String futureDate, Boolean confirm){
        this.title = title;
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
        this.futureDate = futureDate;
        this.futureMillis = futureMillis;
        this.confirm = confirm; // **
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
    public Integer getViews() {
        return views;
    }
    public void setViews(Integer views) {
        this.views = views;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public String getUploadMillis() {
        return uploadMillis;
    }

    public void setUploadMillis(String uploadDate) {
        this.uploadMillis = uploadDate;
    }

    public String getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
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

}
