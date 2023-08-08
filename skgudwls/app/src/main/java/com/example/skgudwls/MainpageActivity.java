package com.example.skgudwls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainpageActivity extends AppCompatActivity {

    private Button imageButton, imageButtona, mypageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        // 버튼 생성
        imageButton = findViewById(R.id.button4);
        imageButtona = findViewById(R.id.button5);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PublicauctionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageButtona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PublicauctionActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        mypageButton = findViewById(R.id.btn_mypage);
//        mypageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
}