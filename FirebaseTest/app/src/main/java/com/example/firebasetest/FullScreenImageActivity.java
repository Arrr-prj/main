package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class FullScreenImageActivity extends AppCompatActivity {

    private ViewPager2 fullScreenViewPager;
    private String[] images;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        fullScreenViewPager = findViewById(R.id.fullScreenViewPager);

        Intent intent = getIntent();
        images = intent.getStringArrayExtra("images");
        position = intent.getIntExtra("position", 0);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, images);
        fullScreenViewPager.setAdapter(imageSliderAdapter);
        fullScreenViewPager.setCurrentItem(position);
    }
}
