//package com.example.firebasetest;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//
//public class LargeImageActivity extends AppCompatActivity {
//
//    private ImageView imageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_large_image);
//
//        // 인텐트에서 이미지 URL을 가져옵니다.
//        String imageUrl = getIntent().getStringExtra("imageUrl");
//
//        imageView = findViewById(R.id.largeImageView);
//
//        // Glide나 Picasso 등의 라이브러리를 사용하여 이미지를 로드하여 이미지뷰에 설정합니다.
//        Glide.with(this).load(imageUrl).into(imageView);
//    }
//}
