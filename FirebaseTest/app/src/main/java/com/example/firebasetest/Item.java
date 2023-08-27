package com.example.firebasetest;

import com.google.api.Billing;

public class Item {
    private String imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    private String id;
    private String price;
    private String category;
    private String info;
    private String seller;
    private String title;
    private String futureMillis;
    private String futureDate;
    private String endPrice;
    private String uploadMillis;
    private Boolean confirm;
    private String buyer;
    private String itemType;
    private String email, name, address, nickname, reports, membership;

    public String getDifferenceDays() {
        return differenceDays;
    }

    public void setDifferenceDays(String differenceDays) {
        this.differenceDays = differenceDays;
    }

    private String differenceDays;
    private Integer views;


    public Item(String title, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, String imageUrl5, String imageUrl6, String id, String price, String endPrice, String category, String info, String seller, String buyer, String futureMillis, String futureDate, String uploadMillis, String differenceDays, Boolean confirm, String itemType, Integer views) {
        this.title = title;
        this.id = id;
        this.price = price;
        this.endPrice = endPrice;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.imageUrl5 = imageUrl5;
        this.imageUrl6 = imageUrl6;
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

    public Item(String title, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, String imageUrl5, String imageUrl6, String id, String price, String endPrice, String category, String info, String seller, String buyer, String futureMillis, String futureDate, String uploadMillis, String differenceDays, Boolean confirm) {
        this.title = title;
        this.id = id;
        this.price = price;
        this.endPrice = endPrice;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.imageUrl5 = imageUrl5;
        this.imageUrl6 = imageUrl6;
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

    public Item() {

    }

    public Item(String title, String imageUrl, String id, String price, String category, String info, String seller, String futureMillis, String futureDate, Boolean confirm) {
        this.title = title;
        this.id = id;
        this.price = price;
        this.imageUrl1 = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
        this.futureDate = futureDate;
        this.futureMillis = futureMillis;
        this.confirm = confirm; // **
    }

    public Item(String title, String imageUrl, String id, String category, String info, String seller, String futureMillis, String futureDate) { // 나눔 아이템 생성자 오버로딩 -> 가격이 빠져있음
        this.title = title;
        this.id = id;
        this.price = "share item";
        this.imageUrl1 = imageUrl;
        this.category = category;
        this.info = info;
        this.seller = seller;
        this.futureDate = futureDate;
        this.futureMillis = futureMillis;
    }

    public Item(String email, String name, String address, String membership, String nickname, String reports) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.membership = membership;
        this.nickname = nickname;
        this.reports = reports;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
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

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }

    public String getImageUrl5() {
        return imageUrl5;
    }

    public void setImageUrl5(String imageUrl5) {
        this.imageUrl5 = imageUrl5;
    }

    public String getImageUrl6() {
        return imageUrl6;
    }

    public void setImageUrl6(String imageUrl6) {
        this.imageUrl6 = imageUrl6;
    }
}
