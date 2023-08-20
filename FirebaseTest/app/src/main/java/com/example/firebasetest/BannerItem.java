package com.example.firebasetest;

public class BannerItem {
    // 홈에서 사용합니다
    private int image; // Drawable resource ID

    public BannerItem(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}